package com.example.project_web_cinema.controller;

import com.example.project_web_cinema.service.HomeService;
import com.example.project_web_cinema.service.MovieService;
import com.example.project_web_cinema.service.PromotionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private final HomeService homeService;

    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("homePage", homeService.getHome());
        return "home";
    }
}
