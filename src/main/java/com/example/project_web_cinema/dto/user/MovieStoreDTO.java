package com.example.project_web_cinema.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MovieStoreDTO {
    private Integer maPhim;
    private String tenPhim;
    private String poster;
    private Integer thoiLuong;
    private String quocGia;
    private Double avgRating;
    private Long voteCount;
}