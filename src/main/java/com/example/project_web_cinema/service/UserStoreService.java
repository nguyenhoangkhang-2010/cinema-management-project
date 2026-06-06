package com.example.project_web_cinema.service;

import com.example.project_web_cinema.dto.user.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserStoreService {
    // Dữ liệu tổng hợp Trang chủ
    HomePageDTO getHomepageData();

    // Các hàm Query danh sách Phim
    List<MovieSliderDTO> getUpcomingMovies();

    List<MovieCardDTO> getTopRatedMovies();

    Page<MovieStoreDTO> getNowShowingMovies(Pageable pageable);

    Page<MovieStoreDTO> searchMovie(String keyword, Pageable pageable);

    MovieStoreDTO getMovieDetail(Integer id);

    // Các hàm Query Khuyến mãi
    List<PromotionDTO> getFeaturedPromotions();

    List<PromotionDTO> getActivePromotions();

    PromotionDTO getPromotionDetail(Integer id);
}