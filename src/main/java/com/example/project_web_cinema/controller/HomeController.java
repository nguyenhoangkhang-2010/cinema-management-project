package com.example.project_web_cinema.controller;

import com.example.project_web_cinema.entity.movie.Movie;
import com.example.project_web_cinema.entity.movie.TrangThaiPhim;
import com.example.project_web_cinema.repository.MovieRepository;
import com.example.project_web_cinema.service.HomeService;
import com.example.project_web_cinema.service.UserStoreService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    private final HomeService homeService;
    private final MovieRepository movieRepository;
    private final UserStoreService userStoreService;

    public HomeController(HomeService homeService, MovieRepository movieRepository, UserStoreService userStoreService) {
        this.homeService = homeService;
        this.movieRepository = movieRepository;
        this.userStoreService = userStoreService;
    }

    @GetMapping({ "/", "/home" })
    public String home(Model model) {
        model.addAttribute("homeData", userStoreService.getHomepageData());

        var dataHome = homeService.getHome();
        if (dataHome != null) {
            model.addAttribute("khuyenMai",
                    dataHome.getKhuyenMai() != null ? dataHome.getKhuyenMai() : new ArrayList<>());
        }

        List<Movie> dsPhimTrangChu = movieRepository.findByTrangThai(TrangThaiPhim.DangChieu);
        model.addAttribute("phimDangChieu", dsPhimTrangChu != null ? dsPhimTrangChu : new ArrayList<>());

        return "user/home";
    }
}
