package com.example.project_web_cinema.controller.admin;

import com.example.project_web_cinema.entity.movie.Movie;
import com.example.project_web_cinema.entity.movie.TrangThaiPhim;
import com.example.project_web_cinema.repository.MovieRepository;
import com.example.project_web_cinema.repository.GenreRepository;
import com.example.project_web_cinema.service.admin.AdminMovieService;
import com.example.project_web_cinema.dto.admin.MovieDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/movies")
public class AdminMovieController {

    private final MovieRepository movieRepository;
    private final AdminMovieService adminMovieService;
    private final GenreRepository genreRepository;

    public AdminMovieController(MovieRepository movieRepository, AdminMovieService adminMovieService,
            GenreRepository genreRepository) {
        this.movieRepository = movieRepository;
        this.adminMovieService = adminMovieService;
        this.genreRepository = genreRepository;
    }

    // 1. Bộ chuyển đổi chuỗi ngày trống thành null để tránh lỗi Data Binding
    @InitBinder
    public void initBinder(org.springframework.web.bind.WebDataBinder binder) {
        var dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(java.time.LocalDate.class,
                new org.springframework.beans.propertyeditors.CustomDateEditor(dateFormat, true) {
                    @Override
                    public void setAsText(String text) throws IllegalArgumentException {
                        if (text == null || text.trim().isEmpty()) {
                            setValue(null);
                        } else {
                            setValue(java.time.LocalDate.parse(text));
                        }
                    }
                });
    }

    // 2. Hiển thị danh sách phim kèm phân trang, tìm kiếm và đẩy phim mới lên đầu
    @GetMapping
    public String listMovies(Model model,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "0") int page) {
        int pageSize = 5;
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(
                page, pageSize, org.springframework.data.domain.Sort.by("maPhim").descending());
        org.springframework.data.domain.Page<Movie> moviePage;

        if (search != null && !search.trim().isEmpty()) {
            moviePage = movieRepository.findByTenPhimContainingIgnoreCaseOrQuocGiaContainingIgnoreCase(search, search,
                    pageable);
            model.addAttribute("searchKeyword", search);
        } else {
            moviePage = movieRepository.findAll(pageable);
            model.addAttribute("searchKeyword", "");
        }

        model.addAttribute("movies", moviePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", moviePage.getTotalPages());

        return "admin/list";
    }

    // 3. Hiển thị giao diện Form thêm phim mới
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("movieDTO", new MovieDTO());
        model.addAttribute("allCategories", genreRepository.findAll());
        return "admin/add_movie";
    }

    // 4. Xử lý lưu thông tin phim mới (Submit Form)
    @PostMapping("/add")
    public String addMovie(@ModelAttribute("movieDTO") MovieDTO movieDTO) {
        try {
            adminMovieService.saveOrUpdateMovie(movieDTO);
            return "redirect:/admin/movies";
        } catch (Exception e) {
            return "redirect:/admin/movies?error=SaveFailed";
        }
    }

    // 5. Hiển thị giao diện Form sửa phim
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        model.addAttribute("movieDTO", adminMovieService.getMovieById(id));
        model.addAttribute("allCategories", genreRepository.findAll());
        return "admin/edit_movie";
    }

    // 6. Xử lý lưu thông tin sau khi sửa (Submit Form)
    @PostMapping("/edit/{id}")
    public String editMovie(@PathVariable Integer id, @ModelAttribute("movieDTO") MovieDTO movieDTO) {
        try {
            movieDTO.setMaPhim(id);
            adminMovieService.saveOrUpdateMovie(movieDTO);
            return "redirect:/admin/movies";
        } catch (Exception e) {
            return "redirect:/admin/movies?error=UpdateFailed";
        }
    }

    // 7. Xử lý xóa phim
    @GetMapping("/delete/{id}")
    public String deleteMovie(@PathVariable Integer id) {
        movieRepository.deleteById(id);
        return "redirect:/admin/movies";
    }

    // 8. Xử lý xóa hàng loạt phim được tích chọn
    @PostMapping("/delete-multiple")
    public String deleteMultipleMovies(
            @RequestParam(value = "movieIds", required = false) java.util.List<Integer> ids) {
        if (ids != null && !ids.isEmpty()) {
            movieRepository.deleteAllById(ids);
        }
        return "redirect:/admin/movies";
    }
}