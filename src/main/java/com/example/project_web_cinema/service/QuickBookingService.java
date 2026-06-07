package com.example.project_web_cinema.service;

import com.example.project_web_cinema.dto.user.*;
import java.time.LocalDate;
import java.util.List;

public interface QuickBookingService {
    List<MovieDropdownDTO> getMovies();

    List<CinemaDropdownDTO> getCinemasByMovie(Integer movieId);

    List<DateDropdownDTO> getDatesByMovieAndCinema(Integer movieId, Integer cinemaId);

    List<ShowtimeDropdownDTO> getShowtimes(Integer movieId, Integer cinemaId, LocalDate date);
}