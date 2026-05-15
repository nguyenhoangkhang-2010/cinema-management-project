package com.example.project_web_cinema.service;

import com.example.project_web_cinema.dto.HomeDTO;
import org.springframework.stereotype.Service;

@Service
public class HomeService {
    private final MovieService movieService;
    private final PromotionService promotionService;

    public HomeService(MovieService movieService, PromotionService promotionService){
        this.movieService = movieService;
        this.promotionService = promotionService;
    }

    public HomeDTO getHome(){
        return HomeDTO.builder()
                .phimDangChieu(movieService.getMoviesDangChieu())
                .khuyenMai(promotionService.getPromotionsHoatDong())
                .build();
    }
}
