package com.nexion.backend.dto;

import com.nexion.backend.enums.TransactionType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryRequest {

    @NotNull(message = "O usuário é obrigatório")
    private Long userId;

    @NotBlank(message = "O nome é obrigatório")
    private String name;

    @NotNull(message = "O tipo é obrigatório")
    private TransactionType type;

    private String color;
    private String icon;
}
