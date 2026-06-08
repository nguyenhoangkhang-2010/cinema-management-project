package com.example.project_web_cinema.dto;

import lombok.Data;
import java.util.List;

@Data
public class PaymentRequestDTO {
    private Integer showtimeId;
    private List<Integer> seatIds;
    private Integer promoId;
    private Double totalAmount;
    private String paymentMethod;
}
