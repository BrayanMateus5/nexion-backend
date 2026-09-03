package com.nexion.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexion.backend.dto.WalletSummaryResponse;
import com.nexion.backend.service.DashboardService;

@RestController
@RequestMapping("/api/v1/wallets/{walletId}/summary")
public class DashboardController {

    private final DashboardService service;

    public DashboardController(DashboardService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<WalletSummaryResponse> resumo(@PathVariable Long walletId) {
        return ResponseEntity.ok(service.resumo(walletId));
    }

}
