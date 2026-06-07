package com.example.project_web_cinema.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatSelectionResponseDTO {
    private Integer showtimeId;
    private String movieName;
    private String poster;
    private String showDate;
    private String showTime;
    private String roomName;
    private String cinemaName;
    private Double basePrice;
    private List<SeatDTO> seats;
}