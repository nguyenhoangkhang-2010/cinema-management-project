package com.example.project_web_cinema.dto;

import lombok.Data;
import java.util.List;

@Data
public class CheckoutSessionDTO {
    private Integer showtimeId;
    private List<Integer> selectedSeatIds;
    private Double originalTotal;
    private Double discountAmount;
    private Double finalTotal;
    private Integer appliedPromoId;

    // Các trường dùng để render Giao diện checkout.html
    private String poster;
    private String movieName;
    private Integer doTuoi;
    private String cinemaName;
    private String roomName;
    private String showTime;
    private String showDate;
    private Double totalPrice;
    private List<SeatDetailDTO> selectedSeats;

    @Data
    public static class SeatDetailDTO {
        private Integer seatId;
        private String seatNumber;
        private String seatType;
        private Double seatTotalPrice;
    }
}