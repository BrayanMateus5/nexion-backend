package com.nexion.backend.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nexion.backend.dto.ForgotPasswordRequest;
import com.nexion.backend.dto.ResetPasswordRequest;
import com.nexion.backend.entity.PasswordResetToken;
import com.nexion.backend.entity.User;
import com.nexion.backend.exception.BusinessException;
import com.nexion.backend.repository.PasswordResetTokenRepository;
import com.nexion.backend.repository.UserRepository;

@Service 
public class PasswordResetService {
    
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository resetTokenRepository;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetService( UserRepository userRepository, PasswordResetTokenRepository resetTokenRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.resetTokenRepository = resetTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void solicitar(ForgotPasswordRequest request) {
        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            PasswordResetToken prt = new PasswordResetToken();
            prt.setToken(UUID.randomUUID().toString());
            prt.setUser(user);
            prt.setExpiresAt(LocalDateTime.now().plusMinutes(30));
            resetTokenRepository.save(prt);

            System.out.println(">>> TOKEN PARA RESET: " + prt.getToken());
        });
    }
    public void redefinir(ResetPasswordRequest request) {
    PasswordResetToken prt = resetTokenRepository.findByToken(request.getToken()).orElseThrow(() -> new BusinessException("Token inválido"));

    if (prt.isUsed()) {
        throw new BusinessException("Este token já foi utilizado");
    }
    if (prt.getExpiresAt().isBefore(LocalDateTime.now())) {
        throw new BusinessException("Token expirado");
    }

    User user = prt.getUser();
    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    userRepository.save(user);

    prt.setUsed(true);
    resetTokenRepository.save(prt);

    }
}
