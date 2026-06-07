package com.example.project_web_cinema.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatDTO {
    private Integer maGhe;
    private String soGhe;
    private String loaiGhe;
    private Double phuThu;
    private Boolean daDat; // Trạng thái BOOKED hay AVAILABLE
}