package com.example.project_web_cinema.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketDTO {
    private Integer id;
    private String tenPhim;
    private String tenPhong;
    private String tenRap;
    private String suatChieu;
    private String danhSachGhe;
    private String trangThai;
}
