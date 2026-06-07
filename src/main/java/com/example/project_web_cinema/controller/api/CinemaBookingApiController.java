package com.example.project_web_cinema.controller.api;

import com.example.project_web_cinema.dto.user.BookingDTOs;
import com.example.project_web_cinema.service.CinemaBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
public class CinemaBookingApiController {

    private final CinemaBookingService cinemaBookingService;

    @GetMapping("/cinemas/by-movie/{movieId}")
    public ResponseEntity<List<BookingDTOs.CinemaResDTO>> getCinemas(@PathVariable Integer movieId) {
        return ResponseEntity.ok(cinemaBookingService.getCinemasByMovie(movieId));
    }

    @GetMapping("/rooms/by-showtime/{showtimeId}")
    public ResponseEntity<List<BookingDTOs.RoomResDTO>> getRooms(@PathVariable Integer showtimeId) {
        return ResponseEntity.ok(cinemaBookingService.getRoomsByShowtime(showtimeId));
    }

    @GetMapping("/seats/{roomId}/{showtimeId}")
    public ResponseEntity<List<BookingDTOs.SeatResDTO>> getSeats(@PathVariable Integer roomId,
            @PathVariable Integer showtimeId) {
        return ResponseEntity.ok(cinemaBookingService.getSeats(roomId, showtimeId));
    }

    @PostMapping("/select-seat")
    public ResponseEntity<?> selectSeat(@RequestBody BookingDTOs.SelectSeatReqDTO req) {
        boolean success = cinemaBookingService.lockSeats(req);
        if (success) {
            return ResponseEntity.ok().body(Map.of("message", "Đã khóa ghế 5 phút để thanh toán"));
        }
        return ResponseEntity.badRequest().body(Map.of("message", "Ghế đã có người chọn trước!"));
    }
}