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
public class TicketQuickBookingServiceImpl implements TicketQuickBookingService {

    private final ShowtimeRepository showtimeRepository;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public List<MovieOptionDTO> getAvailableMovies() {
        return showtimeRepository.findMoviesForQuickBooking().stream()
                .map(row -> new MovieOptionDTO((Integer) row[0], (String) row[1]))
                .collect(Collectors.toList());
    }

    @Override
    public List<CinemaOptionDTO> getAvailableCinemas(Integer movieId) {
        return showtimeRepository.findCinemasForQuickBooking(movieId).stream()
                .map(row -> new CinemaOptionDTO((Integer) row[0], (String) row[1]))
                .collect(Collectors.toList());
    }

    @Override
    public List<DateOptionDTO> getAvailableDates(Integer movieId, Integer cinemaId) {
        return showtimeRepository.findDatesForQuickBooking(movieId, cinemaId).stream()
                .map(date -> new DateOptionDTO(date.toString(), date.format(dateFormatter)))
                .collect(Collectors.toList());
    }

    @Override
    public List<ShowtimeOptionDTO> getAvailableShowtimes(Integer movieId, Integer cinemaId, LocalDate date) {
        return showtimeRepository.findShowtimesForQuickBooking(movieId, cinemaId, date).stream()
                .map(row -> new ShowtimeOptionDTO((Integer) row[0], ((LocalTime) row[1]).format(timeFormatter)))
                .collect(Collectors.toList());
    }
}