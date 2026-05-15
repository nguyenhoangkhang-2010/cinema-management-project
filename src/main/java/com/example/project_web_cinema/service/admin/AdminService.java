package com.example.project_web_cinema.service.admin;

import com.example.project_web_cinema.dto.admin.AdminHomeDTO;
import com.example.project_web_cinema.service.MovieService;
import com.example.project_web_cinema.service.PromotionService;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final MovieService movieService;
    private final PromotionService promotionService;

    public AdminService(MovieService movieService, PromotionService promotionService){
        this.movieService = movieService;
        this.promotionService = promotionService;
    }

    public AdminHomeDTO getAdminHome(){
        AdminHomeDTO admin = new AdminHomeDTO();
        admin.setPhimDangChieu(movieService.getMoviesDangChieu());
        admin.setKhuyenMai(promotionService.getAllPromotions());

        return;
    }
}
