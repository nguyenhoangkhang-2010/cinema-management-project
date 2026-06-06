package com.example.project_web_cinema.controller;

import com.example.project_web_cinema.service.HomeService;
import com.example.project_web_cinema.service.MovieService;
import com.example.project_web_cinema.service.PromotionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;

@Controller
public class HomeController {
    private final HomeService homeService;

    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping("/")
    public String home(Model model) {
        var dataHome = homeService.getHome();

        if (dataHome != null) {
            model.addAttribute("phimDangChieu",
                    dataHome.getPhimDangChieu() != null ? dataHome.getPhimDangChieu() : new ArrayList<>());
            model.addAttribute("khuyenMai",
                    dataHome.getKhuyenMai() != null ? dataHome.getKhuyenMai() : new ArrayList<>());
        }

        return "user/home";
    }
}
