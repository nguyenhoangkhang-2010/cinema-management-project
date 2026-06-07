package com.example.project_web_cinema.dto.user;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class MovieDetailResponseDTO {
    private Integer maPhim;
    private String tenPhim;
    private String poster;
    private String trailer;
    private String daoDien;
    private Integer thoiLuong;
    private LocalDate ngayKhoiChieu;
    private LocalDate ngayKetThucChieu;
    private String quocGia;
    private String moTa;
    private Integer doTuoi;
    private String trangThai;

    // Các List dữ liệu con
    private List<String> theLoai;
    private Double diemDanhGiaTrungBinh;
    private Long tongSoLuotDanhGia;

    private VideoDTO video;
    private List<ScheduleDTO> lichChieu;
    private List<RelatedMovieDTO> phimLienQuan;
}