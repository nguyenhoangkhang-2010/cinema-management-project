package com.example.project_web_cinema.controller.admin;

import com.example.project_web_cinema.entity.promotion.Promotion;
import com.example.project_web_cinema.service.PromotionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        if (!model.containsAttribute("promotion")) {
            model.addAttribute("promotion", new Promotion());
        }
        return "admin/promotions";
    }

    @PostMapping("/save")
    public String savePromotion(@ModelAttribute("promotion") Promotion promotion,
            RedirectAttributes redirectAttributes) {
        try {
            promotionService.save(promotion);
            redirectAttributes.addFlashAttribute("success", "Lưu khuyến mãi thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/promotions";
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public java.util.Map<String, Object> getPromotionApi(@PathVariable Integer id) {
        Promotion p = promotionService.findById(id).orElseThrow();
        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("maKhuyenMai", p.getMaKhuyenMai());
        data.put("tenKhuyenMai", p.getTenKhuyenMai());
        data.put("phanTramGiam", p.getPhanTramGiam());
        data.put("soLuong", p.getSoLuong());
        data.put("ngayBatDau", p.getNgayBatDau());
        data.put("ngayKetThuc", p.getNgayKetThuc());
        data.put("poster", p.getPoster());
        data.put("moTa", p.getMoTa());
        data.put("trangThaiKhuyenMai",
                p.getTrangThaiKhuyenMai() != null ? p.getTrangThaiKhuyenMai().name() : "HoatDong");
        data.put("loaiTaiKhoanToiThieu",
                p.getLoaiTaiKhoanToiThieu() != null ? p.getLoaiTaiKhoanToiThieu().name() : "Thuong");
        return data;
    }

    // 6. Xóa Khuyến mãi
    @GetMapping("/delete/{id}")
    public String deletePromotion(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            promotionService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Xóa khuyến mãi thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể xóa do khuyến mãi này đã có dữ liệu liên kết.");
        }
        return "redirect:/admin/promotions";
    }
}