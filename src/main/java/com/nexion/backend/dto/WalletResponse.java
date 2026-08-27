package com.nexion.backend.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class WalletResponse {
    private Long id;
    private String name;
    private String description;
    private Long ownerId;
    private LocalDateTime createdAt;
}
