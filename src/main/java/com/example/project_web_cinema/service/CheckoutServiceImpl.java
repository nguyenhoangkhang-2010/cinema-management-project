package com.example.project_web_cinema.service;

import com.example.project_web_cinema.dto.user.CheckoutDTO;
import com.example.project_web_cinema.dto.user.SeatCheckoutDTO;
import com.example.project_web_cinema.repository.ShowtimeRepository;
import com.example.project_web_cinema.service.CheckoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {

    private final ShowtimeRepository showtimeRepository;

    @Override
    public CheckoutDTO getCheckoutData(Integer showtimeId, List<Integer> seatIds) {
        List<Object[]> showtimeData = showtimeRepository.getCheckoutShowtimeInfo(showtimeId);
        if (showtimeData.isEmpty()) {
            throw new RuntimeException("Không tìm thấy thông tin suất chiếu!");
        }
        Object[] st = showtimeData.get(0);

        Double basePrice = ((Number) st[6]).doubleValue();

        List<Object[]> seatsData = showtimeRepository.getCheckoutSeatsInfo(seatIds);
        List<SeatCheckoutDTO> selectedSeats = new ArrayList<>();
        Double totalOrderPrice = 0.0;

        for (Object[] seat : seatsData) {
            Double surcharge = ((Number) seat[3]).doubleValue();
            Double seatTotalPrice = basePrice + surcharge;

            selectedSeats.add(SeatCheckoutDTO.builder()
                    .seatId((Integer) seat[0])
                    .seatNumber((String) seat[1])
                    .seatType((String) seat[2])
                    .seatTotalPrice(seatTotalPrice)
                    .build());

            totalOrderPrice += seatTotalPrice;
        }

        return CheckoutDTO.builder()
                .showtimeId(showtimeId)
                .movieName((String) st[0])
                .poster((String) st[1])
                .cinemaName((String) st[2])
                .roomName((String) st[3])
                .showDate(st[4].toString())
                .showTime(st[5].toString())
                .selectedSeats(selectedSeats)
                .totalPrice(totalOrderPrice)
                .doTuoi((Integer) st[7])
                .build();
    }
}