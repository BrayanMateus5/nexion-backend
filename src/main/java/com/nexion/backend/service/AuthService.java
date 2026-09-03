package com.nexion.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import com.nexion.backend.dto.AuthResponse;
import com.nexion.backend.dto.LoginRequest;
import com.nexion.backend.dto.UserRequest;
import com.nexion.backend.dto.UserResponse;
import com.nexion.backend.security.JwtService;

@Service
public class AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Value("${jwt.expiration}")
    private long expiration;

    public AuthService(UserService userService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public UserResponse register(UserRequest request) {
        return userService.criar(request);
        // valida email com a criptografa a senha
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        String token = jwtService.gerarToken(request.getEmail());
        return new AuthResponse(token, "Bearer", expiration / 1000);
    }
}