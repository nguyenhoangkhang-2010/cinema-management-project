package com.example.project_web_cinema.controller.admin;

import com.example.project_web_cinema.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/users")
public class AdminUserRoleController {

    private final UserService userService;

    public AdminUserRoleController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/change-role")
    public String changeRole(@RequestParam("email") String email,
            @RequestParam("role") String role,
            RedirectAttributes redirectAttributes) {
        try {
            userService.changeUserRole(email, role);
            redirectAttributes.addFlashAttribute("success", "Đã cập nhật quyền thành công cho tài khoản: " + email);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi phân quyền: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }
}