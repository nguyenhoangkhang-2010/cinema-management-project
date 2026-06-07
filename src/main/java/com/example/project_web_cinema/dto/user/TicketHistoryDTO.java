package com.example.project_web_cinema.dto.user;

import lombok.Data;

@Data
public class TicketHistoryDTO {
    private Integer maDatVe;
    private String tenPhim;
    private String poster;
    private String ngayChieu;
    private String gioBatDau;
    private String tenPhong;
    private String tenRap;
    private String ngayDat;
    private Double tongTien;
    private String trangThai;
    private String danhSachGhe;
}