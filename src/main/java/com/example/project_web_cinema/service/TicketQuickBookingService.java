package com.example.project_web_cinema.service;

import com.example.project_web_cinema.dto.user.*;
import java.time.LocalDate;
import java.util.List;

public interface TicketQuickBookingService {
    List<MovieOptionDTO> getAvailableMovies();

    List<CinemaOptionDTO> getAvailableCinemas(Integer movieId);

    List<DateOptionDTO> getAvailableDates(Integer movieId, Integer cinemaId);

    List<ShowtimeOptionDTO> getAvailableShowtimes(Integer movieId, Integer cinemaId, LocalDate date);
}