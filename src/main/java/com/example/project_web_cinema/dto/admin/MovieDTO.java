package com.example.project_web_cinema.dto.admin;

import com.example.project_web_cinema.entity.movie.TrangThaiPhim;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class MovieDTO {
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
    private TrangThaiPhim trangThai;
    private Integer doTuoi;

    // Danh sách ID thể loại phim được chọn từ form
    private List<Integer> categoryIds = new ArrayList<>();
}