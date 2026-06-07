package com.example.project_web_cinema.service;

import com.example.project_web_cinema.dto.user.CinemaResponseDTO;
import com.example.project_web_cinema.dto.user.ShowtimeDateResponseDTO;
import com.example.project_web_cinema.dto.user.ShowtimeResponseDTO;
import com.example.project_web_cinema.repository.MovieScreeningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserShowtimeService {

    private final MovieScreeningRepository movieScreeningRepository;

    public List<CinemaResponseDTO> getCinemasByMovie(Integer movieId) {
        List<Object[]> results = movieScreeningRepository.findDistinctCinemasByMovieId(movieId);
        return results.stream()
                .map(row -> new CinemaResponseDTO((Integer) row[0], (String) row[1]))
                .collect(Collectors.toList());
    }

    public List<ShowtimeDateResponseDTO> getDatesByMovieAndCinema(Integer movieId, Integer cinemaId) {
        List<LocalDate> dates = movieScreeningRepository.findDistinctDatesByMovieAndCinema(movieId, cinemaId);
        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("dd/MM");

        return dates.stream()
                .map(date -> new ShowtimeDateResponseDTO(date.toString(), date.format(displayFormatter)))
                .collect(Collectors.toList());
    }

    public List<ShowtimeResponseDTO> getShowtimesByDate(Integer movieId, Integer cinemaId, LocalDate date) {
        List<Object[]> results = movieScreeningRepository.findShowtimesByMovieAndCinemaAndDate(movieId, cinemaId, date);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        return results.stream()
                .map(row -> new ShowtimeResponseDTO((Integer) row[0],
                        ((LocalTime) row[1]).format(timeFormatter),
                        (String) row[2]))
                .collect(Collectors.toList());
    }
}