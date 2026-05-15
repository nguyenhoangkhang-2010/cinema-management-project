package com.example.project_web_cinema.service;

import com.example.project_web_cinema.dto.PromotionDTO;
import com.example.project_web_cinema.entity.promotion.Promotion;
import com.example.project_web_cinema.entity.promotion.TrangThaiKhuyenMai;
import com.example.project_web_cinema.repository.PromotionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromotionService {
    private final PromotionRepository promotionRepository;

    public PromotionService(PromotionRepository promotionRepository){
        this.promotionRepository = promotionRepository;
    }

    public List<PromotionDTO> getAllPromotions() {
        return promotionRepository.findAll()
            .stream()
            .map(p -> PromotionDTO.builder()
                    .maKhuyenMai(p.getMaKhuyenMai())
                    .tenKhuyenMai(p.getTenKhuyenMai())
                    .ngayBatDau(p.getNgayBatDau())
                    .ngayKetThuc(p.getNgayKetThuc())
                    .build()
            )
        .toList();
    }

    public List<PromotionDTO> getPromotionsHoatDong() {
        return promotionRepository.findByTrangThaiKhuyenMai(TrangThaiKhuyenMai.HoatDong)
            .stream()
            .map(p -> PromotionDTO.builder()
                    .maKhuyenMai(p.getMaKhuyenMai())
                    .tenKhuyenMai(p.getTenKhuyenMai())
                    .ngayBatDau(p.getNgayBatDau())
                    .ngayKetThuc(p.getNgayKetThuc())
                    .build()
            )
        .toList();
    }
}
