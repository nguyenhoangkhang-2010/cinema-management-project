package com.example.project_web_cinema.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {
    private Integer maPhong;
    private String tenPhong;
    private Integer soLuongGhe;
    private Integer maRap;
    private String tenRap;
}