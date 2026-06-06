package com.example.project_web_cinema.controller.admin;

import com.example.project_web_cinema.service.admin.AdminPaymentService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/payments")
public class AdminPaymentController {
    private final AdminPaymentService paymentService;

    public AdminPaymentController(AdminPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public String listPayments(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page, Model model) {
        model.addAttribute("payments", paymentService.getPayments(status,
                PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "ngayThanhToan"))));
        model.addAttribute("status", status);
        return "admin/payments";
    }
}