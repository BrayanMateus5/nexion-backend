package com.nexion.backend.dto;

import com.nexion.backend.enums.WalletRole;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateMemberRoleRequest {

    @NotNull(message = "O papel é obrigatório")
    private WalletRole role;
}

// é uma questão de papel do usuário
