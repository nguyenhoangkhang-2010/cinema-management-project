package com.example.project_web_cinema.service;

import com.example.project_web_cinema.dto.CheckoutSessionDTO;
import com.example.project_web_cinema.entity.account.LoaiTaiKhoan;
import com.example.project_web_cinema.entity.promotion.LoaiTaiKhoanToiThieu;
import com.example.project_web_cinema.entity.promotion.Promotion;
import com.example.project_web_cinema.entity.promotion.TrangThaiKhuyenMai;
import com.example.project_web_cinema.repository.BookingPromotionRepository;
import com.example.project_web_cinema.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PromotionService {

    @Autowired
    private BookingPromotionRepository promotionRepository;

    @Autowired
    private PromotionRepository mainPromotionRepository;

    private int accountLevel(LoaiTaiKhoan type) {
        return switch (type) {
            case Thuong -> 1;
            case VIP -> 2;
            case VIP_Pro -> 3;
        };
    }

    private int promoLevel(LoaiTaiKhoanToiThieu type) {
        return switch (type) {
            case Thuong -> 1;
            case VIP -> 2;
            case VIP_Pro -> 3;
        };
    }

    public Promotion validatePromotion(Integer promoId, LoaiTaiKhoan userType) {
        Promotion promo = promotionRepository.findById(promoId)
                .orElseThrow(() -> new RuntimeException("Mã khuyến mãi không tồn tại"));

        LocalDate today = LocalDate.now();
        if (promo.getNgayBatDau() != null && today.isBefore(promo.getNgayBatDau()))
            throw new RuntimeException("Khuyến mãi chưa bắt đầu");
        if (promo.getNgayKetThuc() != null && today.isAfter(promo.getNgayKetThuc()))
            throw new RuntimeException("Khuyến mãi đã hết hạn");
        if (promo.getSoLuong() != null && promo.getSoLuong() <= 0)
            throw new RuntimeException("Khuyến mãi đã hết lượt");
        if (promo.getTrangThaiKhuyenMai() == null || !promo.getTrangThaiKhuyenMai().name().equals("HoatDong")) {
            throw new RuntimeException("Khuyến mãi không hoạt động");
        }
        if (promo.getLoaiTaiKhoanToiThieu() != null) {
            if (accountLevel(userType) < promoLevel(promo.getLoaiTaiKhoanToiThieu())) {
                throw new RuntimeException("Tài khoản không đủ điều kiện sử dụng khuyến mãi");
            }
        }
        return promo;
    }

    public CheckoutSessionDTO applyDiscount(CheckoutSessionDTO session, Integer promoId, LoaiTaiKhoan userType) {
        Promotion promo = validatePromotion(promoId, userType);

        double original = session.getOriginalTotal() != null ? session.getOriginalTotal() : 0;
        double discountPercent = promo.getPhanTramGiam() == null ? 0 : promo.getPhanTramGiam().doubleValue();
        double discount = original * discountPercent / 100.0;

        session.setAppliedPromoId(promoId);
        session.setDiscountAmount(discount);
        session.setFinalTotal(original - discount);
        return session;
    }

    // ==========================================
    // CRUD & ADMIN OPERATIONS
    // ==========================================
    public List<Promotion> getAllPromotions() {
        return mainPromotionRepository.findAll();
    }

    public List<Promotion> searchAndFilterPromotions(String search, String filter, String sort) {
        List<Promotion> promotions = mainPromotionRepository.findAll();

        if (search != null && !search.trim().isEmpty()) {
            String q = search.toLowerCase();
            promotions = promotions.stream()
                    .filter(p -> (p.getTenKhuyenMai() != null && p.getTenKhuyenMai().toLowerCase().contains(q)) ||
                            (p.getMoTa() != null && p.getMoTa().toLowerCase().contains(q)))
                    .collect(Collectors.toList());
        }

        if (filter != null && !filter.trim().isEmpty()) {
            promotions = promotions.stream()
                    .filter(p -> p.getTrangThaiKhuyenMai() != null
                            && p.getTrangThaiKhuyenMai().name().equalsIgnoreCase(filter))
                    .collect(Collectors.toList());
        }

        if (sort != null && !sort.trim().isEmpty()) {
            if (sort.equalsIgnoreCase("newest")) {
                promotions.sort((p1, p2) -> Integer.compare(p2.getMaKhuyenMai(), p1.getMaKhuyenMai()));
            } else if (sort.equalsIgnoreCase("oldest")) {
                promotions.sort(Comparator.comparingInt(Promotion::getMaKhuyenMai));
            }
        }
        return promotions;
    }

    public void save(Promotion promotion) {
        mainPromotionRepository.save(promotion);
    }

    public Optional<Promotion> findById(Integer id) {
        return mainPromotionRepository.findById(id);
    }

    public void deleteById(Integer id) {
        mainPromotionRepository.deleteById(id);
    }

    public List<Promotion> getPromotionsHoatDong() {
        return mainPromotionRepository.findByTrangThaiKhuyenMai(TrangThaiKhuyenMai.HoatDong);
    }
}