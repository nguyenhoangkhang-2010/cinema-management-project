package com.example.project_web_cinema.controller;

import com.example.project_web_cinema.dto.user.*;
import com.example.project_web_cinema.service.TicketQuickBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/ticket")
@RequiredArgsConstructor
public class TicketQuickBookingController {

    private final TicketQuickBookingService quickBookingService;

    @GetMapping("/movies")
    public ResponseEntity<List<MovieOptionDTO>> getMovies() {
        return ResponseEntity.ok(quickBookingService.getAvailableMovies());
    }

    @GetMapping("/raps/{movieId}")
    public ResponseEntity<List<CinemaOptionDTO>> getCinemas(@PathVariable Integer movieId) {
        return ResponseEntity.ok(quickBookingService.getAvailableCinemas(movieId));
    }

    @GetMapping("/dates")
    public ResponseEntity<List<DateOptionDTO>> getDates(@RequestParam Integer movieId, @RequestParam Integer rapId) {
        return ResponseEntity.ok(quickBookingService.getAvailableDates(movieId, rapId));
    }

    @GetMapping("/showtimes")
    public ResponseEntity<List<ShowtimeOptionDTO>> getShowtimes(@RequestParam Integer movieId,
            @RequestParam Integer rapId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(quickBookingService.getAvailableShowtimes(movieId, rapId, date));
    }
}