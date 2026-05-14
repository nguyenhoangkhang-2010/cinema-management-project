package com.example.project_web_cinema.dto;

import com.example.project_web_cinema.entity.movie.TrangThaiPhim;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDTO {
    private Integer maPhim;
    private String tenPhim;
    private String poster;
    private TrangThaiPhim trangThai;
}
