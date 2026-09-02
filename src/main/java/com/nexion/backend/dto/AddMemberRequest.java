package com.nexion.backend.dto;

import com.nexion.backend.enums.WalletRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddMemberRequest {
    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "E-mail inv[alido")
    private String email;

    @NotNull(message = "O papel é obrigatório")
    private WalletRole role;
}
