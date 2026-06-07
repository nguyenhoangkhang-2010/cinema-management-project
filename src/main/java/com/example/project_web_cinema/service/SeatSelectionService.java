package com.example.project_web_cinema.service;

import com.example.project_web_cinema.dto.user.SeatSelectionResponseDTO;

public interface SeatSelectionService {
    SeatSelectionResponseDTO getSeatSelectionData(Integer showtimeId);
}