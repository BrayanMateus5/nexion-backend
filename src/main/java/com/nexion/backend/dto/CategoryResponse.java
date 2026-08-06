package com.nexion.backend.dto;

import com.nexion.backend.enums.TransactionType;

import lombok.Data;

@Data
public class CategoryResponse {
    private Long id;
    private String name;
    private TransactionType type;
    private String color;
    private String icon;
    private Long userId;
}
