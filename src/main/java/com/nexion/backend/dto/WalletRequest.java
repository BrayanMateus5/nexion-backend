package com.nexion.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class WalletRequest {

    @NotBlank(message = "O nome é obrigatório")
    private String name;

    private String description;

}
