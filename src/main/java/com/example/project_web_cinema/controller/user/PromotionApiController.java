package com.example.project_web_cinema.controller.user;

import com.example.project_web_cinema.dto.CheckoutSessionDTO;
import com.example.project_web_cinema.entity.account.LoaiTaiKhoan;
import com.example.project_web_cinema.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/promotion")
public class PromotionApiController {

    @Autowired
    private PromotionService promotionService;

    @PostMapping("/apply/{id}")
    public ResponseEntity<?> applyPromotion(
            @PathVariable("id") Integer promoId,
            @RequestBody Map<String, Double> payload,
            HttpSession session) {
        try {
            CheckoutSessionDTO checkoutSession = (CheckoutSessionDTO) session.getAttribute("CHECKOUT_SESSION");
            if (checkoutSession == null) {
                checkoutSession = new CheckoutSessionDTO();
                checkoutSession.setOriginalTotal(payload.getOrDefault("originalTotal", 0.0));
            }

            // Tự động detect Cấp Bậc từ Security Role
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            LoaiTaiKhoan userAccountType = LoaiTaiKhoan.Thuong;
            if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().contains("VIP_Pro"))) {
                userAccountType = LoaiTaiKhoan.VIP_Pro;
            } else if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().contains("VIP"))) {
                userAccountType = LoaiTaiKhoan.VIP;
            }

            checkoutSession = promotionService.applyDiscount(checkoutSession, promoId, userAccountType);
            session.setAttribute("CHECKOUT_SESSION", checkoutSession);

            return ResponseEntity.ok(Map.of("success", true, "discountAmount", checkoutSession.getDiscountAmount(),
                    "finalTotal", checkoutSession.getFinalTotal()));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}