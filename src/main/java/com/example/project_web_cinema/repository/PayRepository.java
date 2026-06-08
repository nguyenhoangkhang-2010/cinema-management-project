package com.example.project_web_cinema.repository;

import com.example.project_web_cinema.entity.pay.Pay;
import com.example.project_web_cinema.entity.pay.TrangThaiThanhToan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PayRepository extends JpaRepository<Pay, Integer> {

    @Query("SELECT p FROM Pay p " +
            "WHERE (:status IS NULL OR p.trangThai = :status)")
    Page<Pay> findPaymentsWithFilters(@Param("status") TrangThaiThanhToan status, Pageable pageable);

    @Modifying
    @Query(value = "INSERT INTO THANHTOAN (MaDatVe, PhuongThuc, NgayThanhToan, SoTien, TrangThai) VALUES (:maDatVe, :phuongThuc, NOW(), :soTien, :trangThai)", nativeQuery = true)
    void insertPayment(@Param("maDatVe") Integer maDatVe, @Param("phuongThuc") String phuongThuc,
            @Param("soTien") Double soTien, @Param("trangThai") String trangThai);
}