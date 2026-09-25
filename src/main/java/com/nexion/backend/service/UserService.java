package com.nexion.backend.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nexion.backend.dto.UserRequest;
import com.nexion.backend.dto.UserResponse;
import com.nexion.backend.entity.User;
import com.nexion.backend.exception.ResourceNotFoundException;
import com.nexion.backend.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final UserLogService userLogService;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder, UserLogService userLogService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.userLogService = userLogService;
    }

    public UserResponse meuPerfil() {
        return toResponse(userLogService.get());
    }

    public void removerMinhaConta() {
        repository.deleteById(userLogService.get().getId());
    }

    public UserResponse criar(UserRequest request) {
        if (repository.existsByEmail(request.getEmail())) {
            throw new ResourceNotFoundException("E-mail já cadastrado");
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return toResponse(repository.save(user));

    }

    public UserResponse buscarPorEmail(String email) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        return toResponse(user);
    }

    private UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setCreatedAt(user.getCreatedAt());
        return response;
    }
}
