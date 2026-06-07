package com.example.project_web_cinema.service;

import com.example.project_web_cinema.dto.user.CheckoutDTO;
import java.util.List;

public interface CheckoutService {
    CheckoutDTO getCheckoutData(Integer showtimeId, List<Integer> seatIds);
}