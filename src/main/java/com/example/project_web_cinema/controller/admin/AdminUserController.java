package com.example.project_web_cinema.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    // Tiêm UserRepository vào đây
    // private final UserRepository userRepository;
    // public AdminUserController(UserRepository userRepository) {
    // this.userRepository = userRepository; }

    // 1. Hiển thị danh sách khách hàng
    @GetMapping
    public String listUsers(Model model) {
        // Lấy danh sách user từ Database
        // model.addAttribute("users", userRepository.findAll());
        return "admin/users";
    }

    // 2. Nút bấm khóa / mở khóa khách hàng
    @PostMapping("/toggle-lock/{id}")
    public String toggleLockUser(@PathVariable Integer id) {
        // Logic thay đổi cờ trạng thái Hoạt Động của User
        /*
         * User user = userRepository.findById(id).orElse(null);
         * if (user != null) {
         * // Nếu đang True -> False. Đang False -> True
         * user.setHoatDong(!user.isHoatDong());
         * userRepository.save(user);
         * }
         */
        return "redirect:/admin/users";
    }
}