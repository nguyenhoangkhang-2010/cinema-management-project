package com.example.project_web_cinema.dto;

import com.example.project_web_cinema.entity.movie.TrangThaiPhim;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomeDTO {
    private List<MovieDTO> phimDangChieu;
    private List<PromotionDTO> khuyenMai;
}
