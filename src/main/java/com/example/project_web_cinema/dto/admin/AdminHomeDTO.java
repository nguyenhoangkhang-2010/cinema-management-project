package com.example.project_web_cinema.dto.admin;

import com.example.project_web_cinema.dto.MovieDTO;
import com.example.project_web_cinema.dto.PromotionDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminHomeDTO {
    private long totalMovies;
    private long totalUsers;
    private long totalTickets;
    private double totalRevenue;

    private List<MovieDTO> phimDangChieu;
    private List<PromotionDTO> khuyenMai;
}
