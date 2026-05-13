package com.example.project_web_cinema.service;

import com.example.project_web_cinema.dto.HomeDTO;
import com.example.project_web_cinema.entity.movie.TrangThaiPhim;
import com.example.project_web_cinema.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository){
        this.movieRepository = movieRepository;
    }

    public List<HomeDTO> getMoviesDangChieu(){
        return movieRepository.findByTrangThai(TrangThaiPhim.DangChieu)
                .stream()
                .map(movie -> HomeDTO.builder()
                        .maPhim(movie.getMaPhim())
                        .tenPhim(movie.getTenPhim())
                        .poster(movie.getPoster())
                        .trangThai(movie.getTrangThai())
                        .build()
                )
                .toList();
    }
}
