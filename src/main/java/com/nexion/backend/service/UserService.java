package com.nexion.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nexion.backend.entity.User;
import com.nexion.backend.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User criar(User user) {
        if (repository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }
        return repository.save(user);
    }

    public List<User> listarTodos() {
        return repository.findAll();
    }

    public User buscarPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("O usuário não foi encontrado"));
    }

    public User atualizar(Long id, User dados) {
        User user = buscarPorId(id);
        user.setName(dados.getName());
        user.setEmail(dados.getEmail());
        return repository.save(user);
    }

    public void remover(Long id) {
        repository.deleteById(id);
    }
}
