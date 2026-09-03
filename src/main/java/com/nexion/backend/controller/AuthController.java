package com.nexion.backend.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.nexion.backend.dto.AuthResponse;
import com.nexion.backend.dto.LoginRequest;
import com.nexion.backend.dto.UserRequest;
import com.nexion.backend.dto.UserResponse;
import com.nexion.backend.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody @Valid UserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(service.login(request));
    }
}