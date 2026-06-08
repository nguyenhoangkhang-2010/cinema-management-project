package com.example.project_web_cinema.controller;

import com.example.project_web_cinema.entity.movie.Movie;
import com.example.project_web_cinema.entity.movie.TrangThaiPhim;
import com.example.project_web_cinema.repository.MovieRepository;
import com.example.project_web_cinema.repository.VideoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@Controller
public class MovieController {

    private final MovieRepository movieRepository;
    private final VideoRepository videoRepository;

    public MovieController(MovieRepository movieRepository, VideoRepository videoRepository) {
        this.movieRepository = movieRepository;
        this.videoRepository = videoRepository;
    }

    // Trang danh sách Phim Online
    @GetMapping("/phim-online")
    public String onlineMovies(Model model) {
        model.addAttribute("movies", movieRepository.findByTrangThai(TrangThaiPhim.Online));
        return "user/online_movies";
    }

    // Trang Xem Phim Online
    @GetMapping("/watch/{id}")
    public String watchMovie(@PathVariable Integer id, Model model) {
        Movie movie = movieRepository.findById(id).orElse(null);
        // Kiểm tra phim có tồn tại và đúng trạng thái Online không
        if (movie == null || movie.getTrangThai() != TrangThaiPhim.Online) {
            return "redirect:/phim-online?error=NotFound";
        }
        model.addAttribute("movie", movie);

        // Lấy danh sách link Video theo ID Phim (Lấy từ DB thông qua VideoRepository)
        List<Object[]> videos = videoRepository.findVideosByMovieId(id);
        model.addAttribute("videos", videos);

        return "user/watch_movie";
    }
}
