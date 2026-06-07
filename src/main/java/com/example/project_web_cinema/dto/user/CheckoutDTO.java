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
public class CheckoutDTO {
    private Integer showtimeId;
    private String movieName;
    private String poster;
    private Integer doTuoi;
    private String cinemaName;
    private String roomName;
    private String showDate;
    private String showTime;
    private List<SeatCheckoutDTO> selectedSeats;
    private Double totalPrice;
}