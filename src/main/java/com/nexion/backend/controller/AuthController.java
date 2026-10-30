package com.nexion.backend.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.nexion.backend.dto.AuthResponse;
import com.nexion.backend.dto.ForgotPasswordRequest;
import com.nexion.backend.dto.LoginRequest;
import com.nexion.backend.dto.ResetPasswordRequest;
import com.nexion.backend.dto.UserRequest;
import com.nexion.backend.dto.UserResponse;
import com.nexion.backend.service.AuthService;
import com.nexion.backend.service.PasswordResetService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;
    private final PasswordResetService passwordResetService;

    public AuthController(AuthService service, PasswordResetService passwordResetService) {
        this.service = service;
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody @Valid UserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(service.login(request));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestBody @Valid ForgotPasswordRequest request) {
        passwordResetService.solicitar(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        passwordResetService.redefinir(request);
        return ResponseEntity.noContent().build();
    }
}