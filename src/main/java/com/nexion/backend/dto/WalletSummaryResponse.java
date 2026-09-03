package com.nexion.backend.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class WalletSummaryResponse {
    private BigDecimal totalIncome; // Total de receitas, no valor de dinheiro correto
    private BigDecimal totalExpense; // Total de despesas no valor de dinheiro
    private BigDecimal balance;
    private Long transactionCount;
}
