package com.nexion.backend.service;

import org.springframework.stereotype.Service;

import com.nexion.backend.dto.CategoryRequest;
import com.nexion.backend.repository.CategoryRepository;
import com.nexion.backend.repository.UserRepository;

import jakarta.validation.Valid;

@Service
public class CategoryService {

    private final CategoryRepository repository;
    private final UserRepository userRepository;

    public CategoryService(CategoryRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;

    }

}
