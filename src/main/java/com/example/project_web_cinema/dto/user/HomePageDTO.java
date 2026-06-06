package com.example.project_web_cinema.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class HomePageDTO {
    private List<MovieSliderDTO> heroSlider;
    private List<MovieCardDTO> hotMovies;
    private List<MovieSliderDTO> upcomingMovies;
}