package com.example.project_web_cinema.service;

import com.example.project_web_cinema.entity.promotion.Promotion;
import com.example.project_web_cinema.repository.PromotionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PromotionService {

    private final PromotionRepository promotionRepository;

    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    public List<Promotion> getAllPromotions() {
        return promotionRepository.findAllByOrderByMaKhuyenMaiDesc();
    }

    public List<Promotion> searchAndFilterPromotions(String search, String filter, String sort) {
        List<Promotion> list = promotionRepository.findAll();

        if (search != null && !search.trim().isEmpty()) {
            String s = search.toLowerCase().trim();
            list = list.stream()
                    .filter(p -> (p.getMaKhuyenMai() != null && String.valueOf(p.getMaKhuyenMai()).contains(s)) ||
                            (p.getTenKhuyenMai() != null && p.getTenKhuyenMai().toLowerCase().contains(s)))
                    .collect(Collectors.toList());
        }

        LocalDate now = LocalDate.now();
        if (filter != null && !filter.isEmpty() && !filter.equals("ALL")) {
            list = list.stream().filter(p -> {
                boolean isNgung = p.getTrangThaiKhuyenMai() != null && p.getTrangThaiKhuyenMai().name().equals("Ngung");
                boolean isHoatDong = p.getTrangThaiKhuyenMai() != null
                        && p.getTrangThaiKhuyenMai().name().equals("HoatDong");
                boolean isUpcoming = isHoatDong && p.getNgayBatDau() != null && p.getNgayBatDau().isAfter(now);
                boolean isExpired = isHoatDong && p.getNgayKetThuc() != null && p.getNgayKetThuc().isBefore(now);
                boolean isActive = isHoatDong && !isUpcoming && !isExpired;

                switch (filter) {
                    case "ACTIVE":
                        return isActive;
                    case "INACTIVE":
                        return isNgung;
                    case "EXPIRED":
                        return isExpired;
                    case "UPCOMING":
                        return isUpcoming;
                    default:
                        return true;
                }
            }).collect(Collectors.toList());
        }

        if (sort != null && !sort.isEmpty()) {
            Comparator<Promotion> comparator = null;
            switch (sort) {
                case "id_asc":
                    comparator = Comparator.comparing(Promotion::getMaKhuyenMai,
                            Comparator.nullsLast(Comparator.naturalOrder()));
                    break;
                case "id_desc":
                    comparator = Comparator
                            .comparing(Promotion::getMaKhuyenMai, Comparator.nullsLast(Comparator.naturalOrder()))
                            .reversed();
                    break;
                case "start_newest":
                    comparator = Comparator
                            .comparing(Promotion::getNgayBatDau, Comparator.nullsLast(Comparator.naturalOrder()))
                            .reversed();
                    break;
                case "start_oldest":
                    comparator = Comparator.comparing(Promotion::getNgayBatDau,
                            Comparator.nullsLast(Comparator.naturalOrder()));
                    break;
                case "end_nearest":
                    comparator = Comparator.comparing(Promotion::getNgayKetThuc,
                            Comparator.nullsLast(Comparator.naturalOrder()));
                    break;
                case "end_farthest":
                    comparator = Comparator
                            .comparing(Promotion::getNgayKetThuc, Comparator.nullsLast(Comparator.naturalOrder()))
                            .reversed();
                    break;
            }
            if (comparator != null) {
                list.sort(comparator);
            }
        } else {
            list.sort(Comparator.comparing(Promotion::getMaKhuyenMai, Comparator.nullsLast(Comparator.naturalOrder()))
                    .reversed());
        }

        return list;
    }

    public List<Promotion> getPromotionsHoatDong() {
        return getAllPromotions().stream()
                .filter(p -> p.getTrangThaiKhuyenMai() != null && "HoatDong".equals(p.getTrangThaiKhuyenMai().name()))
                .collect(Collectors.toList());
    }

    public void save(Promotion promotion) {
        promotionRepository.save(promotion);
    }

    public Optional<Promotion> findById(Integer id) {
        return promotionRepository.findById(id);
    }

    public void deleteById(Integer id) {
        promotionRepository.deleteById(id);
    }
}