package com.example.project_web_cinema.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PromotionDTO {
    private Integer maKhuyenMai;
    private String tenKhuyenMai;
    private String moTa;
    private BigDecimal phanTramGiam;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;
    private String poster;
}