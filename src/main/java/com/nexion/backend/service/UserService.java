package com.nexion.backend.service;

import java.util.List;

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

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
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

    public List<UserResponse> listarTodos() {
        return repository.findAll().stream().map(this::toResponse).toList(); // percorre e converte em um UserResponse
    }

    public UserResponse buscarPorId(Long id) {
        User user = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        return toResponse(user);
    }

    public UserResponse buscarPorEmail(String email) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        return toResponse(user);
    }

    public void remover(Long id) {
        repository.deleteById(id);
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
