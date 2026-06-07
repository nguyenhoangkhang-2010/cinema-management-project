package com.example.project_web_cinema.service;

import com.example.project_web_cinema.dto.admin.CategoryDTO;
import org.springframework.data.domain.Page;

public interface CategoryService {
    Page<CategoryDTO> searchCategories(String keyword, int page, int size);

    CategoryDTO getCategoryById(Integer id);

    void createCategory(CategoryDTO dto);

    void updateCategory(Integer id, CategoryDTO dto);

    void deleteCategory(Integer id);
}