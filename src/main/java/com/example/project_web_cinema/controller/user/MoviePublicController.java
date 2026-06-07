package com.example.project_web_cinema.controller.user;

import com.example.project_web_cinema.entity.movie.Movie;
import com.example.project_web_cinema.entity.movie.TrangThaiPhim;
import com.example.project_web_cinema.service.MovieService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/movies")
public class MoviePublicController {

    private final MovieService movieService;

    public MoviePublicController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/{type}")
    public String getMovieList(@PathVariable String type,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String quocGia,
            @RequestParam(required = false) Integer doTuoi,
            @RequestParam(defaultValue = "1") int page, Model model) {
        TrangThaiPhim trangThai = switch (type) {
            case "coming-soon" -> TrangThaiPhim.SapChieu;
            case "online" -> TrangThaiPhim.Online;
            default -> TrangThaiPhim.DangChieu;
        };

        Page<Movie> moviePage = movieService.getMoviesList(trangThai, search, quocGia, doTuoi, page);

        model.addAttribute("moviePage", moviePage);
        model.addAttribute("type", type);
        model.addAttribute("pageTitle", trangThai.name().equals("DangChieu") ? "Phim Đang Chiếu"
                : (trangThai.name().equals("SapChieu") ? "Phim Sắp Chiếu" : "Phim Online"));
        return "user/movies-list";
    }

    @GetMapping("/detail/{id}")
    public String getMovieDetail(@PathVariable Integer id, Model model) {
        Movie movie = movieService.findById(id).orElse(null);
        if (movie == null) {
            return "redirect:/movies/showing";
        }
        model.addAttribute("movie", movie);

        // Lấy 4 Phim liên quan để tăng tỷ lệ chuyển đổi
        model.addAttribute("relatedMovies", movieService.getRelatedMovies(id, movie.getQuocGia()));

        return "user/movie-detail";
    }
}