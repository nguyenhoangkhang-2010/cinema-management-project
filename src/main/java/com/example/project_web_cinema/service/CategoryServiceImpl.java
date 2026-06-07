package com.example.project_web_cinema.service;

import com.example.project_web_cinema.dto.admin.CategoryDTO;
import com.example.project_web_cinema.entity.category.Category;
import com.example.project_web_cinema.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final GenreRepository genreRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryDTO> searchCategories(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "maTheLoai"));
        Page<Category> categoryPage;

        if (keyword != null && !keyword.trim().isEmpty()) {
            categoryPage = genreRepository.findByTenLoaiContainingIgnoreCase(keyword.trim(), pageable);
        } else {
            categoryPage = genreRepository.findAll(pageable);
        }

        return categoryPage.map(c -> CategoryDTO.builder()
                .maTheLoai(c.getMaTheLoai())
                .tenLoai(c.getTenLoai())
                .soPhimDangSuDung(c.getDsPhim() != null ? c.getDsPhim().size() : 0)
                .build());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDTO getCategoryById(Integer id) {
        Category c = genreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thể loại phim!"));
        return CategoryDTO.builder()
                .maTheLoai(c.getMaTheLoai())
                .tenLoai(c.getTenLoai())
                .build();
    }

    @Override
    @Transactional
    public void createCategory(CategoryDTO dto) {
        if (genreRepository.existsByTenLoaiIgnoreCase(dto.getTenLoai().trim())) {
            throw new RuntimeException("Tên thể loại đã tồn tại trong hệ thống!");
        }
        Category category = new Category();
        category.setTenLoai(dto.getTenLoai().trim());
        genreRepository.save(category);
    }

    @Override
    @Transactional
    public void updateCategory(Integer id, CategoryDTO dto) {
        if (genreRepository.existsByTenLoaiIgnoreCaseAndMaTheLoaiNot(dto.getTenLoai().trim(), id)) {
            throw new RuntimeException("Tên thể loại đã bị trùng với một thể loại khác!");
        }
        Category category = genreRepository.findById(id).orElseThrow();
        category.setTenLoai(dto.getTenLoai().trim());
        genreRepository.save(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Integer id) {
        Category category = genreRepository.findById(id).orElseThrow();
        if (category.getDsPhim() != null && !category.getDsPhim().isEmpty()) {
            throw new RuntimeException("Không thể xóa vì đang có phim sử dụng thể loại này");
        }
        genreRepository.delete(category);
    }
}