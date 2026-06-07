package com.example.project_web_cinema.controller.api;

import com.example.project_web_cinema.dto.user.*;
import com.example.project_web_cinema.service.QuickBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/quick-booking")
@RequiredArgsConstructor
public class QuickBookingApiController {

    private final QuickBookingService quickBookingService;

    @GetMapping("/movies")
    public ResponseEntity<List<MovieDropdownDTO>> getMovies() {
        return ResponseEntity.ok(quickBookingService.getMovies());
    }

    @GetMapping("/cinemas/{movieId}")
    public ResponseEntity<List<CinemaDropdownDTO>> getCinemas(@PathVariable Integer movieId) {
        return ResponseEntity.ok(quickBookingService.getCinemasByMovie(movieId));
    }

    @GetMapping("/dates")
    public ResponseEntity<List<DateDropdownDTO>> getDates(@RequestParam Integer movieId,
            @RequestParam Integer cinemaId) {
        return ResponseEntity.ok(quickBookingService.getDatesByMovieAndCinema(movieId, cinemaId));
    }

    @GetMapping("/showtimes")
    public ResponseEntity<List<ShowtimeDropdownDTO>> getShowtimes(@RequestParam Integer movieId,
            @RequestParam Integer cinemaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(quickBookingService.getShowtimes(movieId, cinemaId, date));
    }
}