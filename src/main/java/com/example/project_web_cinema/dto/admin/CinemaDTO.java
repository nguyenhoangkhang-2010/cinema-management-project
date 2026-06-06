package com.example.project_web_cinema.dto.admin;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CinemaDTO {
    private Integer maRap;
    private String tenRap;
    private String diaChi;
    private String soDienThoai;

    // Tính toán số lượng phòng chiếu thuộc về rạp này
    private int soLuongPhong;
}