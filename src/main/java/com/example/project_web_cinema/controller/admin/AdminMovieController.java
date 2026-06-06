package com.example.project_web_cinema.controller.admin;

import com.example.project_web_cinema.entity.movie.Movie;
import com.example.project_web_cinema.entity.movie.TrangThaiPhim;
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
    public String listMovies(Model model,
                             @RequestParam(value = "search", required = false) String search,
                             @RequestParam(value = "page", defaultValue = "0") int page) {
        int pageSize = 5; // Số lượng phim hiển thị trên 1 trang
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, pageSize);
        org.springframework.data.domain.Page<Movie> moviePage;

        // Xử lý tìm kiếm nếu có từ khóa nhập vào
        if (search != null && !search.trim().isEmpty()) {
            moviePage = movieRepository.findByTenPhimContainingIgnoreCaseOrQuocGiaContainingIgnoreCase(search, search, pageable);
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

    // 2. Hiển thị giao diện Form thêm phim mới
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("movie", new Movie());
        return "admin/add_movie";
    }

    // 3. Xử lý lưu thông tin phim mới (Submit Form)
    @PostMapping("/add")
    public String addMovie(@ModelAttribute("movie") Movie movie) {
        try {
            if (movie.getNgayKhoiChieu() != null && movie.getNgayKetThucChieu() == null) {
                movie.setNgayKetThucChieu(movie.getNgayKhoiChieu().plusMonths(2));
            }
            if (movie.getDoTuoi() == null) {
                movie.setDoTuoi(0);
            }
            if (movie.getCapDoYeuCau() == null) {
                movie.setCapDoYeuCau(1);
            }
            if (movie.getTrangThai() == null) {
                movie.setTrangThai(TrangThaiPhim.SapChieu);
            }

            movieRepository.save(movie);
            return "redirect:/admin/movies";
        } catch (Exception e) {
            return "redirect:/admin/movies?error=SaveFailed";
        }
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

    // 7. Xử lý xóa hàng loạt phim được tích chọn
    @PostMapping("/delete-multiple")
    public String deleteMultipleMovies(@RequestParam(value = "movieIds", required = false) java.util.List<Integer> ids) {
        if (ids != null && !ids.isEmpty()) {
            movieRepository.deleteAllById(ids);
        }
        return "redirect:/admin/movies";
    }

}