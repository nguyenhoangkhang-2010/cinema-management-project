package com.example.project_web_cinema.service;

import com.example.project_web_cinema.dto.user.TicketHistoryDTO;
import java.util.List;

public interface UserPaymentService {
    Integer processBooking(String email, Integer showtimeId, List<Integer> seatIds, Double totalPrice);

    List<TicketHistoryDTO> getHistory(String email);

    boolean isOrderValid(Integer orderId);
}