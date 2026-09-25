package com.nexion.backend.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.nexion.backend.entity.User;
import com.nexion.backend.exception.ResourceNotFoundException;
import com.nexion.backend.repository.UserRepository;

@Service
public class UserLogService {

    private final UserRepository userRepository;

    public UserLogService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User get() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }

}
