package com.nexion.backend.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.nexion.backend.dto.InsightResponse;
import com.nexion.backend.entity.Transaction;
import com.nexion.backend.enums.TransactionType;
import com.nexion.backend.repository.TransactionRepository;

@Service
public class InsightService {

    private final TransactionRepository transactionRepository;
    private final WalletAccessService walletAccessService;
    private final AiService aiService;
    private static final Logger log = LoggerFactory.getLogger(InsightService.class);

    public InsightService(TransactionRepository repository, WalletAccessService accessService, AiService aiService) {
        this.transactionRepository = repository;
        this.walletAccessService = accessService;
        this.aiService = aiService;
    }

    public InsightResponse gerar(Long walletId) {
        walletAccessService.verificarMembro(walletId);

        List<Transaction> transactions = transactionRepository.findByWalletId(walletId);

        if (transactions.isEmpty()) {
            return new InsightResponse("Cadastre algumas transações para receber a análise dos seus gastos.");
        }
        try {
            return new InsightResponse(aiService.perguntar(montarPrompt(transactions)));
        } catch (Exception e) {
            log.error("Falha ao gerar os insights para a carteira id={}", walletId, e);
            return new InsightResponse("Não foi possível gerar a análise no momento..");

        }
    }

    private String montarPrompt(List<Transaction> transactions) {
        BigDecimal receitas = somar(transactions, TransactionType.INCOME);
        BigDecimal despesas = somar(transactions, TransactionType.EXPENSE);
        BigDecimal saldo = receitas.subtract(despesas);

        String lista = transactions.stream()
                .map(t -> "- %s | %s | R$ %s | %s".formatted(t.getDate(),
                        t.getType() == TransactionType.INCOME ? "receitas" : "despesas", t.getAmount(),
                        t.getDescription()))
                .collect(Collectors.joining("\n"));

        return """
                Você é um assistente financeiro pessoal. Analise as transações abaixo \
                e escreva uma análise curta em português do Brasil, com no máximo 4 frases.
                Destaque o padrão de gastos, a maior despesa e uma sugestão prática.
                Escreva em texto corrido, sem Markdown, sem listas e títulos.
                Use sempre os valores em algarismos, no formato R$ 1.234,56.
                
                Receita total: R$ %s
                Despesa total: R$ %s
                Saldo do período: R$ %s

                Transações:
                %s
                """.formatted(receitas, despesas, saldo, lista);

    }

    // Aqui faz a soma, começando do 0 e vai aumentando
    private BigDecimal somar(List<Transaction> lista, TransactionType tipo) {
        return lista.stream().filter(t -> t.getType() == tipo).map(Transaction::getAmount).reduce(BigDecimal.ZERO,
                BigDecimal::add);
    }
}
