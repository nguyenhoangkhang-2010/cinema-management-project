package com.example.project_web_cinema.service;

import com.example.project_web_cinema.dto.user.SeatDTO;
import com.example.project_web_cinema.repository.ShowtimeRepository;
import com.example.project_web_cinema.service.BookingFlowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingFlowServiceImpl implements BookingFlowService {

    private final ShowtimeRepository showtimeRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getCinemasByMovie(Integer movieId) {
        return showtimeRepository.getCinemasByMovieId(movieId).stream()
                .map(r -> Map.of("id", r[0], "name", r[1])).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getRoomsByCinema(Integer cinemaId) {
        return showtimeRepository.getRoomsByCinemaId(cinemaId).stream()
                .map(r -> Map.of("id", r[0], "name", r[1])).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeatDTO> getSeats(Integer roomId, Integer showtimeId) {
        return showtimeRepository.getSeatsByRoomAndShowtime(roomId, showtimeId).stream()
                .map(r -> SeatDTO.builder()
                        .maGhe((Integer) r[0])
                        .soGhe((String) r[1])
                        .loaiGhe((String) r[2])
                        .phuThu(((Number) r[3]).doubleValue())
                        .daDat(((Number) r[4]).intValue() > 0) // Trạng thái BOOKED vs AVAILABLE
                        .build())
                .collect(Collectors.toList());
    }
}
