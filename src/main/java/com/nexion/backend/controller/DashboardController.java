package com.nexion.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexion.backend.dto.InsightResponse;
import com.nexion.backend.dto.MonthlyReportResponse;
import com.nexion.backend.dto.WalletSummaryResponse;
import com.nexion.backend.service.DashboardService;
import com.nexion.backend.service.InsightService;

@RestController
@RequestMapping("/api/v1/wallets/{walletId}")
public class DashboardController {

    private final DashboardService service;
    private final InsightService insightService;

    public DashboardController(DashboardService service, InsightService insightService) {
        this.service = service;
        this.insightService = insightService;
    }

    @GetMapping("/summary")
    public ResponseEntity<WalletSummaryResponse> resumo(@PathVariable Long walletId) {
        return ResponseEntity.ok(service.resumo(walletId));
    }
    
    @GetMapping("/reports/monthly")
    public ResponseEntity<List<MonthlyReportResponse>> relatorioMensal(@PathVariable Long walletId) {
        return ResponseEntity.ok(service.relatorioMensal(walletId));
    }
    @GetMapping("/insights")
    public ResponseEntity<InsightResponse> insights(@PathVariable Long walletId) {
        return ResponseEntity.ok(insightService.gerar(walletId));
    }
}
