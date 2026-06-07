package com.example.project_web_cinema.service;

import com.example.project_web_cinema.dto.HomeDTO;
import com.example.project_web_cinema.dto.MovieDTO;
import com.example.project_web_cinema.entity.movie.Movie;
import com.example.project_web_cinema.entity.movie.TrangThaiPhim;
import com.example.project_web_cinema.entity.moviescreening.MovieScreening;
import com.example.project_web_cinema.repository.MovieRepository;
import com.example.project_web_cinema.repository.MovieScreeningRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final MovieScreeningRepository movieScreeningRepository;

    public MovieService(MovieRepository movieRepository, MovieScreeningRepository movieScreeningRepository) {
        this.movieRepository = movieRepository;
        this.movieScreeningRepository = movieScreeningRepository;
    }

    public Map<String, List<Movie>> getMegaMenuData() {
        Map<String, List<Movie>> menuData = new HashMap<>();
        menuData.put("dangChieu", getMoviesForTab(TrangThaiPhim.DangChieu));
        menuData.put("sapChieu", getMoviesForTab(TrangThaiPhim.SapChieu));
        menuData.put("online", getMoviesForTab(TrangThaiPhim.Online));
        return menuData;
    }

    public List<Movie> getMoviesForTab(TrangThaiPhim trangThai) {
        // Đẩy thẳng xử lý xuống Database bằng SQL LIMIT 4, tốc độ truy vấn sẽ tăng cực
        // nhanh
        return movieRepository.findTop4ByTrangThaiOrderByNgayKhoiChieuDesc(trangThai);
    }

    public Page<Movie> getMoviesList(TrangThaiPhim trangThai, String search, String quocGia, Integer doTuoi, int page) {
        List<Movie> rawMovies = movieRepository.findByTrangThai(trangThai);
        List<Movie> filtered = rawMovies.stream()
                .filter(m -> search == null || search.trim().isEmpty()
                        || m.getTenPhim().toLowerCase().contains(search.toLowerCase()))
                .filter(m -> quocGia == null || quocGia.trim().isEmpty()
                        || (m.getQuocGia() != null && m.getQuocGia().equalsIgnoreCase(quocGia)))
                .filter(m -> doTuoi == null || m.getDoTuoi() == null || m.getDoTuoi().equals(doTuoi))
                .collect(Collectors.toList());

        int pageSize = 12;
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, filtered.size());
        List<Movie> pageContent = start <= end ? filtered.subList(start, end) : new ArrayList<>();
        return new PageImpl<>(pageContent, PageRequest.of(page - 1, pageSize), filtered.size());
    }

    public Map<LocalDate, List<MovieScreening>> getGroupedShowtimes(Integer maPhim) {
        Movie movie = movieRepository.findById(maPhim).orElse(null);
        if (movie == null)
            return new TreeMap<>();

        List<MovieScreening> screenings = movieScreeningRepository
                .findShowtimesWithFilters(movie.getTenPhim(), null, PageRequest.of(0, 100)).getContent();

        return screenings.stream()
                .filter(s -> s.getNgayChieu() != null && !s.getNgayChieu().isBefore(LocalDate.now()))
                .collect(Collectors.groupingBy(MovieScreening::getNgayChieu, TreeMap::new, Collectors.toList()));
    }

    public Optional<Movie> findById(Integer id) {
        return movieRepository.findById(id);
    }

    public List<MovieDTO> getMoviesDangChieu() {
        return movieRepository.findByTrangThai(TrangThaiPhim.DangChieu)
                .stream()
                .map(movie -> MovieDTO.builder()
                        .maPhim(movie.getMaPhim())
                        .tenPhim(movie.getTenPhim())
                        .poster(movie.getPoster())
                        .trangThai(movie.getTrangThai())
                        .build())
                .toList();
    }

    public List<MovieDTO> getMoviesSapChieu() {
        return movieRepository.findByTrangThai(TrangThaiPhim.SapChieu)
                .stream()
                .map(movie -> MovieDTO.builder()
                        .maPhim(movie.getMaPhim())
                        .tenPhim(movie.getTenPhim())
                        .poster(movie.getPoster())
                        .trangThai(movie.getTrangThai())
                        .build())
                .toList();
    }

    public List<MovieDTO> getMoviesNgungChieu() {
        return movieRepository.findByTrangThai(TrangThaiPhim.NgungChieu)
                .stream()
                .map(movie -> MovieDTO.builder()
                        .maPhim(movie.getMaPhim())
                        .tenPhim(movie.getTenPhim())
                        .poster(movie.getPoster())
                        .trangThai(movie.getTrangThai())
                        .build())
                .toList();
    }

    public List<MovieDTO> getAllMovies() {
        return movieRepository.findAll()
                .stream()
                .map(movie -> MovieDTO.builder()
                        .maPhim(movie.getMaPhim())
                        .tenPhim(movie.getTenPhim())
                        .poster(movie.getPoster())
                        .trangThai(movie.getTrangThai())
                        .build())
                .toList();
    }

    public long countAllMovies() {
        return movieRepository.count();
    }
}
