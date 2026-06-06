package com.example.project_web_cinema.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDTO {
    private Integer maDatVe;

    private String hoTenKhachHang;
    private String email;
    private String soDienThoai;

    private String tenPhim;
    private String poster;
    private String ngayChieu;
    private String gioBatDau;
    private String tenPhong;

    private String danhSachGhe;

    private LocalDateTime ngayDat;
    private BigDecimal tongTien;
    private String tenKhuyenMai;
    private String phuongThucThanhToan;
    private String trangThaiDatVe;
    private String trangThaiThanhToan;
}