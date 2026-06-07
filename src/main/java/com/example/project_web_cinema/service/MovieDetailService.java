package com.example.project_web_cinema.service;

import com.example.project_web_cinema.dto.user.MovieDetailResponseDTO;

public interface MovieDetailService {
    MovieDetailResponseDTO getMovieDetail(Integer movieId);
}