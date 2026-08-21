package com.nexion.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nexion.backend.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUserId(Long userId);

}
