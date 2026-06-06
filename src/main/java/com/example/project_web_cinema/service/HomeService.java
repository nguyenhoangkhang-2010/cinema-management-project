package com.example.project_web_cinema.service;

import com.example.project_web_cinema.dto.HomeDTO;
import com.example.project_web_cinema.dto.PromotionDTO;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class HomeService {
    private final MovieService movieService;
    private final PromotionService promotionService;

    public HomeService(MovieService movieService, PromotionService promotionService) {
        this.movieService = movieService;
        this.promotionService = promotionService;
    }

    public HomeDTO getHome() {
        return HomeDTO.builder()
                .phimDangChieu(movieService.getMoviesDangChieu())
                .khuyenMai(promotionService.getPromotionsHoatDong().stream()
                        .map(p -> PromotionDTO.builder()
                                .maKhuyenMai(p.getMaKhuyenMai())
                                .tenKhuyenMai(p.getTenKhuyenMai())
                                .phanTramGiam(p.getPhanTramGiam())
                                .poster(p.getPoster())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
