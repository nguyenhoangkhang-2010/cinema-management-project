package com.example.project_web_cinema.controller;

import com.example.project_web_cinema.dto.UserDTO;
import com.example.project_web_cinema.repository.AccountRepository;
import com.example.project_web_cinema.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;

@Controller
public class ProfileController {
    private final UserService userService;
    private final AccountRepository accountRepository;

    public ProfileController(UserService userService, AccountRepository accountRepository) {
        this.userService = userService;
        this.accountRepository = accountRepository;
    }

    @GetMapping("/profile")
    public String showProfilePage(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String currentHoTen = authentication.getName();
        String currentEmail = accountRepository.findAll().stream()
                .filter(acc -> acc.getHoTen().equals(currentHoTen))
                .map(acc -> acc.getEmail())
                .findFirst()
                .orElse(currentHoTen);
        try {
            UserDTO userProfile = userService.getProfileByEmail(currentEmail);

            if (userProfile.getDanhSachVe() == null) {
                userProfile.setDanhSachVe(new ArrayList<>());
            }

            model.addAttribute("user", userProfile);
        } catch (Exception e) {
            UserDTO fallbackUser = UserDTO.builder()
                    .hoTen(currentHoTen)
                    .email("chua_co_email@cinema.com")
                    .vaiTro("User")
                    .loaiTaiKhoan("Thuong")
                    .trangThai("HoatDong")
                    .danhSachVe(new ArrayList<>())
                    .build();

            model.addAttribute("user", fallbackUser);
        }
        return "user/profile";
    }
}
