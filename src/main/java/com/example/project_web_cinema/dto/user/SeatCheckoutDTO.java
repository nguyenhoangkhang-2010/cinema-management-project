package com.example.project_web_cinema.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatCheckoutDTO {
    private Integer seatId;
    private String seatNumber;
    private String seatType;
    private Double seatTotalPrice; // Giá vé gốc + Phụ thu
}