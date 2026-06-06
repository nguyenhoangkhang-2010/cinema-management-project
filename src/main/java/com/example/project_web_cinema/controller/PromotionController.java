package com.example.project_web_cinema.controller;

import com.example.project_web_cinema.entity.promotion.Promotion;
import com.example.project_web_cinema.service.PromotionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/promotions")
public class PromotionController {

    private final PromotionService promotionService;

    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    private Map<String, Object> mapToDTO(Promotion p) {
        Map<String, Object> dto = new HashMap<>();
        dto.put("maKhuyenMai", p.getMaKhuyenMai());
        dto.put("tenKhuyenMai", p.getTenKhuyenMai());
        dto.put("phanTramGiam", p.getPhanTramGiam());
        dto.put("ngayBatDau", p.getNgayBatDau());
        dto.put("ngayKetThuc", p.getNgayKetThuc());
        dto.put("poster", p.getPoster());
        dto.put("moTa", p.getMoTa());
        dto.put("soLuong", p.getSoLuong());
        dto.put("loaiTaiKhoanToiThieu", p.getLoaiTaiKhoanToiThieu());

        LocalDate now = LocalDate.now();
        String trangThaiHienThi = "UNKNOWN";
        boolean isNgung = p.getTrangThaiKhuyenMai() != null && p.getTrangThaiKhuyenMai().name().equals("Ngung");
        boolean isHoatDong = p.getTrangThaiKhuyenMai() != null && p.getTrangThaiKhuyenMai().name().equals("HoatDong");
        boolean isUpcoming = isHoatDong && p.getNgayBatDau() != null && p.getNgayBatDau().isAfter(now);
        boolean isExpired = isHoatDong && p.getNgayKetThuc() != null && p.getNgayKetThuc().isBefore(now);
        boolean isActive = isHoatDong && !isUpcoming && !isExpired;

        if (isNgung)
            trangThaiHienThi = "INACTIVE";
        else if (isUpcoming)
            trangThaiHienThi = "UPCOMING";
        else if (isExpired)
            trangThaiHienThi = "EXPIRED";
        else if (isActive)
            trangThaiHienThi = "ACTIVE";

        dto.put("trangThaiHienThi", trangThaiHienThi);
        return dto;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllPromotions() {
        return ResponseEntity
                .ok(promotionService.getAllPromotions().stream().map(this::mapToDTO).collect(Collectors.toList()));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Map<String, Object>>> searchPromotions(@RequestParam String q) {
        return ResponseEntity.ok(promotionService.searchAndFilterPromotions(q, null, null).stream().map(this::mapToDTO)
                .collect(Collectors.toList()));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Map<String, Object>>> filterPromotions(@RequestParam String status) {
        return ResponseEntity.ok(promotionService.searchAndFilterPromotions(null, status, null).stream()
                .map(this::mapToDTO).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getPromotionById(@PathVariable Integer id) {
        return promotionService.findById(id).map(p -> ResponseEntity.ok(mapToDTO(p)))
                .orElse(ResponseEntity.notFound().build());
    }
}
