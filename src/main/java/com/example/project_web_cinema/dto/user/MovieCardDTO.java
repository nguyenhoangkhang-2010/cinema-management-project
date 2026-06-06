package com.example.project_web_cinema.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MovieCardDTO {
    private Integer maPhim;
    private String tenPhim;
    private String poster;
    private Integer thoiLuong;
    private Double avgRating;
    private Long voteCount;
}