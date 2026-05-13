package com.example.project_web_cinema.controller;

import com.example.project_web_cinema.service.MovieService;
import com.example.project_web_cinema.service.PromotionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private final MovieService movieService;
    private final PromotionService promotionService;

    public HomeController(MovieService movieService, PromotionService promotionService) {
        this.movieService = movieService;
        this.promotionService = promotionService;
    }

    @GetMapping("/home")
    public String home(Model model) {

        model.addAttribute("movies", movieService.getMoviesDangChieu());
        model.addAttribute("promotions", promotionService.getAllPromotions());
        return "home";
    }
}
