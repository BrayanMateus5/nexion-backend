package com.nexion.backend.dto;

import lombok.Data;

@Data
public class AuthResponse {

    private String accessToken;
    private String tokenType;
    private Long expiresIn;
    private UserResponse user;

    public AuthResponse( String accesToken, String tokenType, Long expiresIn, UserResponse user) {
        this.accessToken = accesToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
        this.user = user;
    }
}