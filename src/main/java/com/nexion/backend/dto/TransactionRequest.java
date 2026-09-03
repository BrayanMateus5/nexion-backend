package com.nexion.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.nexion.backend.enums.TransactionType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

@Data
public class TransactionRequest {

    @NotNull(message = "O tipo é obrigatório")
    private TransactionType type;

    @NotNull(message = "O valor é obrigatório")
    @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero")
    private BigDecimal amount;

    private String description;

    @NotNull(message = "A data é obrigatória")
    @PastOrPresent(message = "A data não pode ser futura")
    private LocalDate date;

    private Long categoryId;

    @NotNull(message = "O autor é obrigatório")
    private Long createdById;
}
