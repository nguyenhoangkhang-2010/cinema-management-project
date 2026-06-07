package com.example.project_web_cinema.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowtimeResponseDTO {
    private Integer maSuatChieu;
    private String gioBatDau; // "09:00"
    private String tenPhong; // "Phòng 1"
}