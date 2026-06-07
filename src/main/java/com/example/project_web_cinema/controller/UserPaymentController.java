package com.example.project_web_cinema.controller;

import com.example.project_web_cinema.service.UserPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserPaymentController {

    private final UserPaymentService userPaymentService;

    @PostMapping("/payment/process")
    public String processPayment(@RequestParam Integer showtimeId, @RequestParam List<Integer> seats,
            @RequestParam Double totalPrice, Authentication authentication) {
        try {
            Integer orderId = userPaymentService.processBooking(authentication.getName(), showtimeId, seats,
                    totalPrice);
            return "redirect:/payment/success?orderId=" + orderId;
        } catch (Exception e) {
            return "redirect:/?error=PaymentFailed";
        }
    }

    @GetMapping("/payment/success")
    public String paymentSuccess(@RequestParam Integer orderId, Model model) {
        // Thay vì dùng .get() gây lỗi 500, ta kiểm tra qua Service
        if (orderId == null || !userPaymentService.isOrderValid(orderId)) {
            return "redirect:/?error=OrderNotFound"; // Redirect về trang chủ kèm lỗi
        }

        model.addAttribute("orderId", orderId);
        return "user/payment-success";
    }

    @GetMapping("/user/history")
    public String bookingHistory(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login"; // Bắt buộc đăng nhập
        }
        try {
            model.addAttribute("history", userPaymentService.getHistory(authentication.getName()));
        } catch (Exception e) {
            model.addAttribute("history", java.util.Collections.emptyList());
        }
        return "user/history";
    }
}