package com.nexion.backend.service;

import java.time.LocalDateTime;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final EmailService emailService;
    private static final Logger log = 
    LoggerFactory.getLogger(PasswordResetService.class);

    public PasswordResetService( UserRepository userRepository, PasswordResetTokenRepository resetTokenRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.resetTokenRepository = resetTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public void solicitar(ForgotPasswordRequest request) {
        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            PasswordResetToken prt = new PasswordResetToken();
            prt.setToken(UUID.randomUUID().toString());
            prt.setUser(user);
            prt.setExpiresAt(LocalDateTime.now().plusMinutes(30));
            resetTokenRepository.save(prt);
            try {
            emailService.enviarResetSenha(user.getEmail(), prt.getToken());
            }catch (Exception e) {
                log.error("Falha ao enviar o e-mail de redefinição para o usuário id={}", user.getId(), e);
            }
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
