package com.example.project_web_cinema.controller;

import com.example.project_web_cinema.entity.movie.TrangThaiPhim;
import com.example.project_web_cinema.repository.MovieRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MovieController {

    private final MovieRepository movieRepository;

    public MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    // Trang danh sách Phim Online
    @GetMapping("/phim-online")
    public String onlineMovies(Model model) {
        model.addAttribute("movies", movieRepository.findByTrangThai(TrangThaiPhim.Online));
        return "user/online_movies";
    }
}
