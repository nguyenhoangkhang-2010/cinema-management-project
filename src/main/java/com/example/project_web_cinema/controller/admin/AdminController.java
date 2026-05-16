package com.example.project_web_cinema.controller.admin;

import com.example.project_web_cinema.dto.admin.AdminHomeDTO;
import com.example.project_web_cinema.service.admin.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService){
        this.adminService = adminService;
    }

    @GetMapping("/admin/home")
    public String adminHome(Model model){
        AdminHomeDTO admin = adminService.getAdminHome();

        model.addAttribute("admin", admin);
        model.addAttribute("homePage", admin);
        return "admin/admin_home";
    }
}
