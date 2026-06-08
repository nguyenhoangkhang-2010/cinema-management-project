package com.example.project_web_cinema.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewResponseDTO {
    private String hoTen;
    private Integer soSao;
    private String binhLuan;
    private String ngayDanhGia;
}