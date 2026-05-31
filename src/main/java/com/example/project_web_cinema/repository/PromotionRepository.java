package com.example.project_web_cinema.repository;

import com.example.project_web_cinema.entity.promotion.Promotion;
import com.example.project_web_cinema.entity.promotion.TrangThaiKhuyenMai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Integer> {
    List<Promotion> findByTrangThaiKhuyenMai(TrangThaiKhuyenMai trangThaiKhuyenMai);
}
