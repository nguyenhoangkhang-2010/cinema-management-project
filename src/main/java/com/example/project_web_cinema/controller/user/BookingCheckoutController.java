package com.example.project_web_cinema.controller.user;

import com.example.project_web_cinema.entity.account.LoaiTaiKhoan;
import com.example.project_web_cinema.entity.promotion.LoaiTaiKhoanToiThieu;
import com.example.project_web_cinema.dto.PaymentRequestDTO;
import com.example.project_web_cinema.service.BookingPromotionService;
import com.example.project_web_cinema.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

@Controller
public class BookingCheckoutController {

    @Autowired
    private BookingPromotionService bookingPromotionService;

    @Autowired
    private PromotionService promotionService;

    @GetMapping("/checkout")
    public String showCheckoutPage(
            @RequestParam(required = false) Integer promoId,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        Object checkoutSession = session.getAttribute("CHECKOUT_SESSION");
        if (checkoutSession == null) {
            if (promoId != null) {
                // Lưu mã KM vào Session và điều hướng sang trang Đặt Phim
                session.setAttribute("SAVED_PROMO_ID", promoId);
                redirectAttributes.addFlashAttribute("success",
                        "Đã lưu mã! Vui lòng chọn phim và ghế để hệ thống áp dụng giảm giá.");
                return "redirect:/movies/showing";
            }
            redirectAttributes.addFlashAttribute("error", "Vui lòng chọn phim và ghế trước khi thanh toán!");
            return "redirect:/";
        }

        // Nếu có mã ưu đãi đã lưu từ trước, tự động nhúng vào URL
        if (promoId == null && session.getAttribute("SAVED_PROMO_ID") != null) {
            return "redirect:/checkout?promoId=" + session.getAttribute("SAVED_PROMO_ID");
        }

        model.addAttribute("checkout", checkoutSession);
        model.addAttribute("activePromotions", promotionService.getPromotionsHoatDong());
        return "user/checkout";
    }

    @PostMapping("/payment/confirm")
    public String confirmPayment(
            @ModelAttribute PaymentRequestDTO paymentRequest,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        try {

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = auth.getName(); // Lấy email từ phiên đăng nhập

            LoaiTaiKhoan userType = LoaiTaiKhoan.Thuong;

            if (auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().contains("VIP_Pro"))) {
                userType = LoaiTaiKhoan.VIP_Pro;

            } else if (auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().contains("VIP"))) {
                userType = LoaiTaiKhoan.VIP;
            }

            Integer orderId = bookingPromotionService.createBookingWithPromotion(paymentRequest, userType, userEmail);

            session.removeAttribute("CHECKOUT_SESSION");
            session.removeAttribute("SAVED_PROMO_ID"); // Xóa mã tạm sau khi thanh toán thành công

            return "redirect:/payment/success?orderId=" + orderId;

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/checkout";
        }
    }
}