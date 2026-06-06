package com.example.project_web_cinema.controller.admin;

import com.example.project_web_cinema.entity.promotion.Promotion;
import com.example.project_web_cinema.service.PromotionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/promotions")
public class AdminPromotionController {

    private final PromotionService promotionService;

    public AdminPromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    // 1. Hiển thị danh sách khuyến mãi
    @GetMapping
    public String listPromotions(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) String sort,
            Model model) {
        model.addAttribute("promotions", promotionService.searchAndFilterPromotions(search, filter, sort));
        model.addAttribute("search", search);
        model.addAttribute("filter", filter);
        model.addAttribute("sort", sort);
        return "admin/promotions";
    }

    // 2. Gọi giao diện Form thêm mới
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("promotion", new Promotion());
        return "admin/promotion/add";
    }

    // 3. Nhận dữ liệu từ HTML và Lưu vào Database
    @PostMapping("/add")
    public String addPromotion(@ModelAttribute("promotion") Promotion promotion) {
        promotionService.save(promotion);
        return "redirect:/admin/promotions";
    }

    // 4. Hiển thị giao diện Form sửa
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Promotion promotion = promotionService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid promotion Id:" + id));
        model.addAttribute("promotion", promotion);
        return "admin/promotion/edit";
    }

    // 5. Xử lý lưu thông tin sau khi sửa
    @PostMapping("/edit/{id}")
    public String updatePromotion(@PathVariable Integer id, @ModelAttribute("promotion") Promotion promotion) {
        promotion.setMaKhuyenMai(id); // Ensure the ID is set for update
        promotionService.save(promotion);
        return "redirect:/admin/promotions";
    }

    // 6. Xóa Khuyến mãi
    @GetMapping("/delete/{id}")
    public String deletePromotion(@PathVariable Integer id) {
        promotionService.deleteById(id);
        return "redirect:/admin/promotions";
    }
}