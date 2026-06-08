package com.example.project_web_cinema.repository;

import com.example.project_web_cinema.entity.promotion.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingPromotionRepository extends JpaRepository<Promotion, Integer> {

    @Modifying
    @Query("UPDATE Promotion p SET p.soLuong = p.soLuong - 1 WHERE p.maKhuyenMai = :id AND p.soLuong > 0")
    int decreaseQuantityIfAvailable(@Param("id") Integer id);
}