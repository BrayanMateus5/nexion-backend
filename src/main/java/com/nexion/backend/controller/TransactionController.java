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

import com.nexion.backend.dto.TransactionRequest;
import com.nexion.backend.dto.TransactionResponse;
import com.nexion.backend.service.TransactionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/wallets/{walletId}/transactions")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> criar(@PathVariable Long walletId,
            @RequestBody @Valid TransactionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(walletId, request));
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> listar(@PathVariable Long walletId) {
        return ResponseEntity.ok(service.listarPorCateira(walletId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> buscarPorId(@PathVariable Long walletId, @PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> atualizar(@PathVariable Long walletId, @PathVariable Long id,
            @RequestBody @Valid TransactionRequest request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long walletId, @PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
