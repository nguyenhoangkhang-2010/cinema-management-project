package com.example.project_web_cinema.service;

import com.example.project_web_cinema.dto.user.SeatDTO;
import java.util.List;
import java.util.Map;

public interface BookingFlowService {
    List<Map<String, Object>> getCinemasByMovie(Integer movieId);

    List<Map<String, Object>> getRoomsByCinema(Integer cinemaId);

    List<SeatDTO> getSeats(Integer roomId, Integer showtimeId);
}
