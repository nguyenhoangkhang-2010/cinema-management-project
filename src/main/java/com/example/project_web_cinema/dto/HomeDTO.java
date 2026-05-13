package com.example.project_web_cinema.dto;

import com.example.project_web_cinema.entity.movie.TrangThaiPhim;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomeDTO {
    private Integer maPhim;
    private String tenPhim;
    private String poster;
    private TrangThaiPhim trangThai;
    private Integer maKhuyenMai;
    private String tenKhuyenMai;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;
}
