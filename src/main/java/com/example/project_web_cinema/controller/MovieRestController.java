package com.example.project_web_cinema.controller;

import com.example.project_web_cinema.entity.movie.Movie;
import com.example.project_web_cinema.entity.movie.TrangThaiPhim;
import com.example.project_web_cinema.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/movies")
public class MovieRestController {
    private final MovieService movieService;

    public MovieRestController(MovieService movieService) {
        this.movieService = movieService;
    }

    // Hàm chuyển đổi Entity thành Map để tránh vòng lặp vô hạn (Infinite Recursion)
    // khi Serialize JSON
    private Map<String, Object> mapToDTO(Movie m) {
        Map<String, Object> dto = new HashMap<>();
        dto.put("maPhim", m.getMaPhim());
        dto.put("tenPhim", m.getTenPhim());
        dto.put("poster", m.getPoster());
        dto.put("thoiLuong", m.getThoiLuong());
        dto.put("quocGia", m.getQuocGia());
        dto.put("doTuoi", m.getDoTuoi());
        return dto;
    }

    @GetMapping("/mega-menu")
    public ResponseEntity<Map<String, List<Map<String, Object>>>> getMegaMenu() {
        Map<String, List<Movie>> rawData = movieService.getMegaMenuData();

        // Chỉ trả về các trường cần thiết, bỏ qua các mối quan hệ @OneToMany
        Map<String, List<Map<String, Object>>> response = new HashMap<>();
        response.put("dangChieu", rawData.get("dangChieu").stream().map(this::mapToDTO).collect(Collectors.toList()));
        response.put("sapChieu", rawData.get("sapChieu").stream().map(this::mapToDTO).collect(Collectors.toList()));
        response.put("online", rawData.get("online").stream().map(this::mapToDTO).collect(Collectors.toList()));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/tab")
    public ResponseEntity<List<Map<String, Object>>> getTabMovies(@RequestParam String status) {
        TrangThaiPhim trangThai = TrangThaiPhim.valueOf(status);

        List<Map<String, Object>> response = movieService.getMoviesForTab(trangThai)
                .stream().map(this::mapToDTO).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}