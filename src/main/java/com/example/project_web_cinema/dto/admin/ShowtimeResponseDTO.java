package com.example.project_web_cinema.dto.admin;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class ShowtimeResponseDTO {
    private Integer maSuatChieu;
    private String tenPhim;
    private String tenPhong;
    private LocalDate ngayChieu;
    private LocalTime gioBatDau;
    private LocalTime gioKetThuc;
    private BigDecimal giaVe;
}