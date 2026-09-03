package com.nexion.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.nexion.backend.enums.TransactionType;

import lombok.Data;

@Data
public class TransactionResponse {
    private Long id;
    private Long walletId;
    private Long categoryId;
    private Long createdById;
    private TransactionType type;
    private BigDecimal amount;
    private String description;
    private LocalDate date;
}
