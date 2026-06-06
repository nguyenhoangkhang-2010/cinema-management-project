package com.example.project_web_cinema.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromotionDTO {
    private Integer maKhuyenMai;
    private String tenKhuyenMai;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;

    private String moTa;
    private String poster;

    private BigDecimal phanTramGiam;
}
