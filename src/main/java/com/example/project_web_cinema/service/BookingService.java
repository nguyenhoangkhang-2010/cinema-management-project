package com.example.project_web_cinema.service;

import com.example.project_web_cinema.dto.PaymentRequestDTO;
import com.example.project_web_cinema.entity.account.Account;
import com.example.project_web_cinema.repository.BookingRepository;
import com.example.project_web_cinema.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final TicketRepository ticketRepository;

    @Transactional
    public Integer createBooking(Account account, PaymentRequestDTO request, Integer promoId) {
        bookingRepository.insertBooking(
                account.getMaTaiKhoan(),
                "DaThanhToan",
                request.getTotalAmount(),
                promoId);

        Integer maDatVe = bookingRepository.getLastInsertId();

        for (Integer seatId : request.getSeatIds()) {
            ticketRepository.insertTicket(maDatVe, request.getShowtimeId(), seatId);
        }

        return maDatVe;
    }
}
