package com.example.project_web_cinema.service.admin;

import com.example.project_web_cinema.dto.admin.MovieDTO;

public interface AdminMovieService {

    void saveOrUpdateMovie(MovieDTO dto);

    MovieDTO getMovieById(Integer id);
}