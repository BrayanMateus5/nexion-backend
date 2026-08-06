package com.nexion.backend.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserResponse {
    private Long Id;
    private String name;
    private String email;
    private LocalDateTime createdAt;
}
