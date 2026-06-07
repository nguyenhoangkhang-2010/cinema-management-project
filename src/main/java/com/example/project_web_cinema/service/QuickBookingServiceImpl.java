package com.example.project_web_cinema.service;

import com.example.project_web_cinema.dto.user.*;
import com.example.project_web_cinema.repository.ShowtimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuickBookingServiceImpl implements QuickBookingService {

    private final ShowtimeRepository showtimeRepository;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public List<MovieDropdownDTO> getMovies() {
        return showtimeRepository.findMoviesForQuickBooking().stream()
                .map(row -> new MovieDropdownDTO((Integer) row[0], (String) row[1]))
                .collect(Collectors.toList());
    }

    @Override
    public List<CinemaDropdownDTO> getCinemasByMovie(Integer movieId) {
        return showtimeRepository.findCinemasForQuickBooking(movieId).stream()
                .map(row -> new CinemaDropdownDTO((Integer) row[0], (String) row[1]))
                .collect(Collectors.toList());
    }

    @Override
    public List<DateDropdownDTO> getDatesByMovieAndCinema(Integer movieId, Integer cinemaId) {
        return showtimeRepository.findDatesForQuickBooking(movieId, cinemaId).stream()
                .map(date -> new DateDropdownDTO(date.toString(), date.format(dateFormatter)))
                .collect(Collectors.toList());
    }

    @Override
    public List<ShowtimeDropdownDTO> getShowtimes(Integer movieId, Integer cinemaId, LocalDate date) {
        return showtimeRepository.findShowtimesForQuickBooking(movieId, cinemaId, date).stream()
                .map(row -> new ShowtimeDropdownDTO((Integer) row[0], ((LocalTime) row[1]).format(timeFormatter)))
                .collect(Collectors.toList());
    }
}