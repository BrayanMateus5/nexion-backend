package com.nexion.backend.dto;

import com.nexion.backend.enums.WalletRole;

import lombok.Data;

@Data
public class MemberResponse {
    private Long userId;
    private String nome;
    private String email;
    private WalletRole role;
}
