package com.example.project_web_cinema.controller.admin;

import com.example.project_web_cinema.entity.movie.Movie;
import com.example.project_web_cinema.repository.MovieRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/movies")
public class AdminMovieController {

    private final MovieRepository movieRepository;

    public AdminMovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    // 1. Hiển thị danh sách phim
    @GetMapping
    public String listMovies(Model model) {
        // Lấy danh sách phim thật từ database
        model.addAttribute("movies", movieRepository.findAll());
        return "admin/list";
    }

    // 2. Hiển thị giao diện Form thêm phim mới
    @GetMapping("/add")
    public String showAddForm() {
        return "admin/movie/add";
    }

    // 3. Xử lý lưu thông tin phim mới (Submit Form)
    @PostMapping("/add")
    public String addMovie(@ModelAttribute Movie movie) {
        movieRepository.save(movie); // Lưu trực tiếp vào MySQL
        return "redirect:/admin/movies";
    }

    // 4. Hiển thị giao diện Form sửa phim
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        // Tìm phim cũ để đưa lên giao diện
        model.addAttribute("movie", movieRepository.findById(id).orElse(null));
        return "admin/movie/edit";
    }

    // 5. Xử lý lưu thông tin sau khi sửa (Submit Form)
    @PostMapping("/edit/{id}")
    public String editMovie(@PathVariable Integer id, @ModelAttribute Movie movie) {
        movie.setMaPhim(id); // Ép ID cũ để Hibernate hiểu đây là lệnh Update chứ không phải thêm mới
        movieRepository.save(movie);
        return "redirect:/admin/movies";
    }

    // 6. Xử lý xóa phim
    @GetMapping("/delete/{id}")
    public String deleteMovie(@PathVariable Integer id) {
        movieRepository.deleteById(id);
        return "redirect:/admin/movies";
    }
}