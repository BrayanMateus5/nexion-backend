package com.nexion.backend.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.nexion.backend.dto.CategoryRequest;
import com.nexion.backend.dto.CategoryResponse;
import com.nexion.backend.entity.Category;
import com.nexion.backend.entity.User;
import com.nexion.backend.exception.ResourceNotFoundException;
import com.nexion.backend.repository.CategoryRepository;

@Service
public class CategoryService {

    private final CategoryRepository repository;
    private final UserLogService userLogService;

    public CategoryService(CategoryRepository repository, UserLogService userLogService) {
        this.repository = repository;
        this.userLogService = userLogService;
    }

    public CategoryResponse criar(CategoryRequest request) {
        User user = userLogService.get();

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
        Long userId = userLogService.get().getId();
        return repository.findByUserId(userId).stream().map(this::toResponse).toList();
    }

    public CategoryResponse buscarPorId(Long id) {
        Category category = buscarEntidade(id);
        verificarDono(category);
        return toResponse(category);
    }

    public void remover(Long id) {
        Category category = buscarEntidade(id);
        verificarDono(category);
        repository.delete(category);
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

    private Category buscarEntidade(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
    }

    private void verificarDono(Category category) {
        Long userId = userLogService.get().getId();

        if (!category.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Categoria não encontrada");
        }
    }

}
