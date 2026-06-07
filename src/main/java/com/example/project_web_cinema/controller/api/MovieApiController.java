package com.example.project_web_cinema.controller.api;

import com.example.project_web_cinema.dto.user.CinemaResponseDTO;
import com.example.project_web_cinema.dto.user.ShowtimeDateResponseDTO;
import com.example.project_web_cinema.dto.user.ShowtimeResponseDTO;
import com.example.project_web_cinema.service.UserShowtimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieApiController {

    private final UserShowtimeService userShowtimeService;

    @GetMapping("/{id}/cinemas")
    public ResponseEntity<List<CinemaResponseDTO>> getCinemas(@PathVariable Integer id) {
        return ResponseEntity.ok(userShowtimeService.getCinemasByMovie(id));
    }

    @GetMapping("/{id}/dates")
    public ResponseEntity<List<ShowtimeDateResponseDTO>> getDates(@PathVariable Integer id,
            @RequestParam Integer cinemaId) {
        return ResponseEntity.ok(userShowtimeService.getDatesByMovieAndCinema(id, cinemaId));
    }

    @GetMapping("/{id}/showtimes")
    public ResponseEntity<List<ShowtimeResponseDTO>> getShowtimes(
            @PathVariable Integer id, @RequestParam Integer cinemaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(userShowtimeService.getShowtimesByDate(id, cinemaId, date));
    }
}