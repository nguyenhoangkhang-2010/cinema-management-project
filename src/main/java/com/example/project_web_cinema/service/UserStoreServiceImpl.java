package com.example.project_web_cinema.service;

import com.example.project_web_cinema.dto.user.*;
import com.example.project_web_cinema.entity.movie.Movie;
import com.example.project_web_cinema.entity.movie.TrangThaiPhim;
import com.example.project_web_cinema.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserStoreServiceImpl implements UserStoreService {

    private final MovieRepository movieRepository;

    @Autowired
    public UserStoreServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public HomePageDTO getHomepageData() {
        // Truy vấn danh sách Phim Sắp Chiếu (Dùng Native Query đã có sẵn)
        List<Object[]> upcomingNative = movieRepository.findUpcomingMoviesNative(TrangThaiPhim.SapChieu.name(),
                TrangThaiPhim.SapChieu.ordinal());
        List<MovieSliderDTO> allUpcoming = new ArrayList<>();

        for (Object[] row : upcomingNative) {
            java.time.LocalDate ngayKhoiChieu = null;
            if (row[3] != null) {
                if (row[3] instanceof java.sql.Date) {
                    ngayKhoiChieu = ((java.sql.Date) row[3]).toLocalDate();
                } else {
                    ngayKhoiChieu = java.time.LocalDate.parse(row[3].toString());
                }
            }
            allUpcoming.add(new MovieSliderDTO(
                    (Integer) row[0],
                    (String) row[1],
                    (String) row[2],
                    ngayKhoiChieu,
                    (String) row[4]));
        }

        // A. Hero Slider: Lấy tối đa 5 phim sắp chiếu
        List<MovieSliderDTO> heroSlider = allUpcoming.stream().limit(5).collect(Collectors.toList());

        // B. Phim đang chiếu nổi bật: Dùng Native Query đã có sẵn (Top Rating)
        List<Object[]> hotRaw = movieRepository.findTopRatedNowShowingMoviesNative(TrangThaiPhim.DangChieu.name(),
                TrangThaiPhim.DangChieu.ordinal());
        List<MovieCardDTO> hotMovies = new ArrayList<>();
        for (Object[] row : hotRaw) {
            hotMovies.add(new MovieCardDTO(
                    (Integer) row[0],
                    (String) row[1],
                    (String) row[2],
                    (Integer) row[3],
                    ((Number) row[4]).doubleValue(),
                    ((Number) row[5]).longValue()));
        }

        // DỰ PHÒNG THÔNG MINH: Nếu không có Phim Sắp Chiếu, tự động dùng Phim Đang
        // Chiếu làm Banner
        if (heroSlider.isEmpty() && !hotRaw.isEmpty()) {
            for (Object[] row : hotRaw) {
                if (heroSlider.size() >= 5)
                    break;
                heroSlider.add(new MovieSliderDTO(
                        (Integer) row[0],
                        (String) row[1],
                        (String) row[2],
                        null, // Không có dữ liệu Ngày Khởi Chiếu do phim đã chiếu
                        null // Không lấy Trailer trong query này
                ));
            }
        }

        // C. Phim sắp chiếu: Lấy tối đa 6 phim sắp chiếu
        List<MovieSliderDTO> upcomingMovies = allUpcoming.stream().limit(6).collect(Collectors.toList());

        return new HomePageDTO(heroSlider, hotMovies, upcomingMovies);
    }

    @Override
    public List<MovieSliderDTO> getUpcomingMovies() {
        return new ArrayList<>();
    }

    @Override
    public List<MovieCardDTO> getTopRatedMovies() {
        return new ArrayList<>();
    }

    @Override
    public Page<MovieStoreDTO> getNowShowingMovies(Pageable pageable) {
        return null;
    }

    @Override
    public Page<MovieStoreDTO> searchMovie(String keyword, Pageable pageable) {
        return null;
    }

    @Override
    public MovieStoreDTO getMovieDetail(Integer id) {
        return null;
    }

    @Override
    public List<PromotionDTO> getFeaturedPromotions() {
        return null;
    }

    @Override
    public List<PromotionDTO> getActivePromotions() {
        return null;
    }

    @Override
    public PromotionDTO getPromotionDetail(Integer id) {
        return null;
    }
}