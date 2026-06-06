package com.example.project_web_cinema.dto.user;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Data
@Builder
public class UserMovieDTO {
    private Integer maPhim;
    private String tenPhim;
    private String poster;
    private String daoDien;
    private String quocGia;
    private Integer thoiLuong;
    private LocalDate ngayKhoiChieu;
    private String trangThai;

    // Tích hợp hệ thống Đánh giá sau này (Prepared Logic)
    private Double diemDanhGia;
    private Integer luotDanhGia;

    public long getNgayConLai() {
        if (ngayKhoiChieu != null) {
            long days = ChronoUnit.DAYS.between(LocalDate.now(), ngayKhoiChieu);
            return days > 0 ? days : 0;
        }
        return 0;
    }
}