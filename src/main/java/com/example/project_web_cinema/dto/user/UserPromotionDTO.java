package com.example.project_web_cinema.dto.user;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.math.BigDecimal;

@Data
@Builder
public class UserPromotionDTO {
    private Integer maKhuyenMai;
    private String tenKhuyenMai;
    private String moTa;
    private String poster;
    private BigDecimal phanTramGiam;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;
    private String trangThaiKhuyenMai;
}