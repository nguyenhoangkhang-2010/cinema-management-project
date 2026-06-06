package com.example.project_web_cinema.repository;

import com.example.project_web_cinema.entity.promotion.Promotion;
import com.example.project_web_cinema.entity.promotion.TrangThaiKhuyenMai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Integer> {
    List<Promotion> findByTrangThaiKhuyenMai(TrangThaiKhuyenMai trangThaiKhuyenMai);

    List<Promotion> findAllByOrderByMaKhuyenMaiDesc();

    @Query(value = "SELECT MaKhuyenMai, TenKhuyenMai, MoTa, PhanTramGiam, NgayBatDau, NgayKetThuc, Poster " +
            "FROM KHUYENMAI WHERE TrangThai = 'HoatDong' AND CURRENT_DATE() BETWEEN NgayBatDau AND NgayKetThuc", nativeQuery = true)
    List<Object[]> findActivePromotionsNative();

    @Query(value = "SELECT MaKhuyenMai, TenKhuyenMai, MoTa, PhanTramGiam, NgayBatDau, NgayKetThuc, Poster " +
            "FROM KHUYENMAI WHERE TrangThai = 'HoatDong' ORDER BY PhanTramGiam DESC LIMIT 3", nativeQuery = true)
    List<Object[]> findFeaturedPromotionsNative();
}
