package com.example.project_web_cinema.controller;

import com.example.project_web_cinema.dto.user.CheckoutDTO;
import com.example.project_web_cinema.dto.CheckoutSessionDTO;
import com.example.project_web_cinema.service.CheckoutService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutService checkoutService;

    @GetMapping("/checkout/{showtimeId}")
    public String prepareCheckoutSession(@PathVariable Integer showtimeId,
            @RequestParam("seats") List<Integer> seatIds,
            HttpSession session) {

        CheckoutDTO checkoutData = checkoutService.getCheckoutData(showtimeId, seatIds);

        CheckoutSessionDTO sessionDTO = new CheckoutSessionDTO();
        sessionDTO.setShowtimeId(checkoutData.getShowtimeId());
        sessionDTO.setOriginalTotal(checkoutData.getTotalPrice());
        sessionDTO.setTotalPrice(checkoutData.getTotalPrice());
        sessionDTO.setFinalTotal(checkoutData.getTotalPrice());
        sessionDTO.setDiscountAmount(0.0);
        sessionDTO.setPoster(checkoutData.getPoster());
        sessionDTO.setMovieName(checkoutData.getMovieName());
        sessionDTO.setDoTuoi(checkoutData.getDoTuoi());
        sessionDTO.setCinemaName(checkoutData.getCinemaName());
        sessionDTO.setRoomName(checkoutData.getRoomName());
        sessionDTO.setShowTime(checkoutData.getShowTime());
        sessionDTO.setShowDate(checkoutData.getShowDate());

        List<CheckoutSessionDTO.SeatDetailDTO> seatDetails = checkoutData.getSelectedSeats().stream().map(s -> {
            CheckoutSessionDTO.SeatDetailDTO sd = new CheckoutSessionDTO.SeatDetailDTO();
            sd.setSeatId(s.getSeatId());
            sd.setSeatNumber(s.getSeatNumber());
            sd.setSeatType(s.getSeatType());
            sd.setSeatTotalPrice(s.getSeatTotalPrice());
            return sd;
        }).collect(Collectors.toList());

        sessionDTO.setSelectedSeats(seatDetails);
        sessionDTO.setSelectedSeatIds(seatIds);

        session.setAttribute("CHECKOUT_SESSION", sessionDTO);

        return "redirect:/checkout";
    }
}