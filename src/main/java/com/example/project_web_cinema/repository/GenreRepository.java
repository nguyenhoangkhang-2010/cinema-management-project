package com.example.project_web_cinema.repository;

import com.example.project_web_cinema.entity.category.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface GenreRepository extends JpaRepository<Category, Integer> {
    // JOIN bảng PHIM_THELOAI để lấy danh sách tên Thể Loại
    @Query("SELECT g.tenLoai FROM Category g JOIN g.dsPhim m WHERE m.maPhim = :movieId")
    List<String> findGenreNamesByMovieId(@Param("movieId") Integer movieId);

    Page<Category> findByTenLoaiContainingIgnoreCase(String tenLoai, Pageable pageable);

    boolean existsByTenLoaiIgnoreCase(String tenLoai);

    boolean existsByTenLoaiIgnoreCaseAndMaTheLoaiNot(String tenLoai, Integer maTheLoai);
}