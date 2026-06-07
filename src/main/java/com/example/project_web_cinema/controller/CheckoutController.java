package com.example.project_web_cinema.controller;

import com.example.project_web_cinema.dto.user.CheckoutDTO;
import com.example.project_web_cinema.service.CheckoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutService checkoutService;

    @GetMapping("/checkout/{showtimeId}")
    public String showCheckoutPage(@PathVariable Integer showtimeId,
            @RequestParam("seats") List<Integer> seatIds,
            Model model) {

        CheckoutDTO checkoutData = checkoutService.getCheckoutData(showtimeId, seatIds);
        model.addAttribute("checkout", checkoutData);
        return "user/checkout";
    }
}