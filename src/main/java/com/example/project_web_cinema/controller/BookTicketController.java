package com.example.project_web_cinema.controller;

import com.example.project_web_cinema.service.SeatSelectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class BookTicketController {

    private final SeatSelectionService seatSelectionService;

    @GetMapping("/book/showtime/{id}")
    public String showSeatSelection(@PathVariable Integer id, Model model) {
        try {
            model.addAttribute("data", seatSelectionService.getSeatSelectionData(id));
            return "user/seat-selection";
        } catch (Exception e) {
            return "redirect:/?error=ShowtimeNotFound";
        }
    }
}