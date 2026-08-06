package com.nexion.backend.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexion.backend.entity.User;
import com.nexion.backend.service.UserService;

@RestController // responde a requisição do HTTP com JSON
@RequestMapping("/api/v1/users") // caminho de todos os métodos
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping // Criar
    public ResponseEntity<User> criar(@RequestBody User user) {
        User criado = service.criar(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @GetMapping // buscar
    public ResponseEntity<List<User>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}") // buscar por ID
    public ResponseEntity<User> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}") // atualizar com ID
    public ResponseEntity<User> atualizar(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(service.atualizar(id, user));
    }

    @DeleteMapping("/{id}") // remove o ID
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
