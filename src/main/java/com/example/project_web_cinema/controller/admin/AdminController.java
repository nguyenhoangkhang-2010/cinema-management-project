package com.example.project_web_cinema.controller.admin;

import com.example.project_web_cinema.dto.admin.AdminHomeDTO;
import com.example.project_web_cinema.service.admin.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/admin/home")
    public String adminHome() {
        return "redirect:/";
    }

    // Đón lỏng đường dẫn /home cũ và bẻ lái về trang chủ chính (/)
    @GetMapping("/home")
    public String oldHomeRedirect() {
        return "redirect:/";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("admin", adminService.getAdminHome());
        return "admin/admin_dashboard";
    }

    @GetMapping("/admin/promotions")
    public String showPromotionList(Model model) {
        model.addAttribute("promotions", adminService.getAllPromotions());
        return "admin/promotion_list";
    }

    @GetMapping("/admin/api/chart-data/{type}")
    @ResponseBody
    public Map<String, Object> getChartData(@PathVariable String type) {
        return adminService.getChartDataByType(type);
    }
}
