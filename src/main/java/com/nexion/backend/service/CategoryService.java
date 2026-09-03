package com.nexion.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nexion.backend.dto.CategoryRequest;
import com.nexion.backend.dto.CategoryResponse;
import com.nexion.backend.entity.Category;
import com.nexion.backend.entity.User;
import com.nexion.backend.exception.ResourceNotFoundException;
import com.nexion.backend.repository.CategoryRepository;
import com.nexion.backend.repository.UserRepository;

@Service
public class CategoryService {

    private final CategoryRepository repository;
    private final UserRepository userRepository;

    public CategoryService(CategoryRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public CategoryResponse criar(CategoryRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        Category category = new Category();
        category.setUser(user);
        category.setName(request.getName());
        category.setType(request.getType());
        category.setColor(request.getColor());
        category.setIcon(request.getIcon());
        // cores e icones usados no front

        return toResponse(repository.save(category));
    }

    public List<CategoryResponse> listarTodos() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    public CategoryResponse buscarPorId(Long id) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
        return toResponse(category);
    }

    public void remover(Long id) {
        repository.deleteById(id);
    }

    private CategoryResponse toResponse(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setType(category.getType());
        response.setColor(category.getColor());
        response.setIcon(category.getIcon());
        response.setUserId(category.getUser().getId());
        return response;
    }

}
