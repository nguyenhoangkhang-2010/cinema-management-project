package com.example.project_web_cinema.service;

import com.example.project_web_cinema.dto.PaymentRequestDTO;
import com.example.project_web_cinema.entity.account.LoaiTaiKhoan;
import com.example.project_web_cinema.entity.promotion.Promotion;
import com.example.project_web_cinema.entity.account.Account;
import com.example.project_web_cinema.repository.AccountRepository;
import com.example.project_web_cinema.repository.BookingPromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
public class BookingPromotionService {

    @Autowired
    private BookingPromotionRepository promotionRepository;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PromotionService promotionService;

    // =========================
    // CREATE BOOKING FLOW
    // =========================
    @Transactional
    public Integer createBookingWithPromotion(PaymentRequestDTO request,
            LoaiTaiKhoan userType, String userEmail) {

        Promotion promo = null;

        if (request.getPromoId() != null) {

            promo = promotionService.validatePromotion(request.getPromoId(), userType);

            int updated = promotionRepository
                    .decreaseQuantityIfAvailable(request.getPromoId());

            if (updated == 0) {
                throw new RuntimeException("Khuyến mãi vừa hết lượt");
            }
        }

        // =========================
        // FLOW CHUẨN (IMPORTANT)
        // =========================
        Account account = accountRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin tài khoản người dùng!"));

        Integer maDatVe = bookingService.createBooking(account, request, request.getPromoId());
        paymentService.createPayment(maDatVe, request.getPaymentMethod(), request.getTotalAmount());

        return maDatVe;
    }
}