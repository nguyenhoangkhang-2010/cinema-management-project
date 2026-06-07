package com.example.project_web_cinema.dto.user;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ScheduleDTO {
    private Integer maSuatChieu;
    private LocalDate ngayChieu;
    private LocalTime gioBatDau;
    private LocalTime gioKetThuc;
    private Double giaVe;
    private String tenPhong;
    private String tenRap;
    private String diaChiRap;
}