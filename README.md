# Nexion — Backend

API REST do **Nexion**, um sistema de gestão financeira pessoal e compartilhada. 
Construído em Spring Boot com arquitetura em camadas, autenticação JWT, DTOs, validação e tratamento global de exceções.

## Tecnologias

- **Java 17** + **Spring Boot 3.3.5**
- **Spring Security + JWT** (jjwt) — autenticação e autorização
- **Spring Data JPA / Hibernate** — persistência
- **MySQL** — banco de dados
- **Bean Validation** — validação de entrada
- **SpringDoc / Swagger** — documentação da API
- **Lombok** — redução de boilerplate
- **Maven** — build e dependências

## Arquitetura (camadas)

```
controller  → recebe requisições HTTP, retorna ResponseEntity
service     → lógica de negócio e validações
repository  → acesso ao banco (Spring Data JPA)
entity      → classes mapeadas para as tabelas
dto         → objetos de transferência (request/response)
exception   → exceções customizadas + handler global (@RestControllerAdvice)
security    → JWT (filtro, geração/validação de token, UserDetailsService)
config      → Spring Security + CORS
enums       → TransactionType (INCOME/EXPENSE), WalletRole (OWNER/EDITOR/VIEWER)
```

## Pré-requisitos

- Java 17 (JDK)
- MySQL em execução na porta 3306 (ex: via XAMPP)
- Maven (ou o wrapper `mvnw` incluído)

### Variáveis de ambiente / configuração

| Variável | Descrição |
|---|---|
| `spring.datasource.url` | `jdbc:mysql://localhost:3306/nexion?createDatabaseIfNotExist=true` |
| `spring.datasource.username` | usuário do MySQL (ex: `admin`) |
| `spring.datasource.password` | senha do MySQL (no arquivo de secrets) |
| `jwt.secret` | chave para assinar os tokens JWT (mín. 256 bits) |
| `jwt.expiration` | validade do token em ms (`86400000` = 24h) |

## Como executar

```bash
mvnw spring-boot:run
```
A API sobe em `http://localhost:8080`.

## Documentação (Swagger)

Com a aplicação rodando:
```
http://localhost:8080/swagger-ui.html
```

## Autenticação (JWT)

1. `POST /auth/register` — cria a conta (senha criptografada com BCrypt)
2. `POST /auth/login` — retorna um `accessToken` (JWT, validade 24h)
3. Envie o token nas rotas protegidas no header: `Authorization: Bearer <token>`
4. Um filtro (`OncePerRequestFilter`) valida o JWT a cada requisição

Rotas públicas: `/auth/**`, `/swagger-ui/**`, `/v3/api-docs/**`. As demais exigem token.

## Principais endpoints

| Método | Rota | Descrição |
|---|---|---|
| POST | `/auth/register` | cria usuário |
| POST | `/auth/login` | autentica e retorna JWT |
| GET/POST/PUT/DELETE | `/api/v1/categories` | CRUD de categorias |
| GET/POST/GET/DELETE | `/api/v1/wallets` | CRUD de carteiras |
| GET/POST/PATCH/DELETE | `/api/v1/wallets/{id}/members` | membros da carteira |
| GET/POST/GET/PUT/DELETE | `/api/v1/wallets/{walletId}/transactions` | transações |
| GET | `/api/v1/wallets/{walletId}/summary` | resumo financeiro |

## Decisões de projeto

- **Entidades e endpoints em inglês** (`User`, `Category`, `Wallet`, `Transaction`):
  alinham com os endpoints da especificação (`/categories` → `Category`) e com o
  frontend, mantendo o modelo coeso num só idioma.

- **Relacionamento de membros unidirecional** (`WalletMember` → `Wallet`/`User`):
  em vez de uma lista `@OneToMany` na carteira, os membros são consultados via
  repository. É mais simples, evita recursão do Lombok em relações bidirecionais
  e mantém a mesma modelagem.

- **DTOs em vez de expor entidades**: os DTOs de resposta não incluem a senha
  (segurança), os de request carregam as validações (`@NotBlank`, `@Email`, etc.)
  e desacoplam o modelo do banco da API.

- **`@Transactional` ao criar carteira**: criar a carteira e adicionar o dono como
  membro OWNER é uma operação única — ou as duas acontecem, ou nenhuma (rollback).

- **`@RestControllerAdvice` global**: um único ponto traduz exceções em status HTTP
  corretos (404, 409, 422, 400) com um JSON padronizado, mantendo os controllers
  e services livres de tratamento de erro.

- **Senhas com BCrypt**: nunca armazenadas em texto puro; comparadas via
  `PasswordEncoder` no login.

## Status

- ✅ CRUDs completos (usuários, categorias, carteiras, membros, transações, resumo)
- ✅ Segurança JWT (register, login, proteção de rotas, BCrypt)
- ✅ DTOs, validação, tratamento global de exceções, CORS, Swagger
- ✅ Integração com o frontend (autenticação e transações consomem a API real)
- 🔜 Recuperação de senha por e-mail (endpoints ainda não implementados)
