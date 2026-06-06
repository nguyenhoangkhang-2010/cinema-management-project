package com.example.project_web_cinema.controller.user;

import com.example.project_web_cinema.repository.PromotionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("userPromotionController")
public class PromotionController {

    // Tiêm Repository vào để lấy dữ liệu
    private final PromotionRepository promotionRepository;

    public PromotionController(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    @GetMapping("/khuyen-mai")
    public String viewPromotions(Model model) {
        // Lấy danh sách khuyến mãi từ CSDL truyền xuống View
        model.addAttribute("khuyenMai", promotionRepository.findAll());

        return "user/promotions";
    }
}