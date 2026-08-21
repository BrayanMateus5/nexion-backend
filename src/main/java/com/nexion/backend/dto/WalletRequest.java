package com.nexion.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WalletRequest {

    @NotNull(message = "O dono é obrigatório")
    private Long ownerId;

    @NotBlank(message = "O nome é obrigatório")
    private String name;

    private String description;

}
