package com.example.project_web_cinema.controller;

import com.example.project_web_cinema.dto.UserDTO;
import com.example.project_web_cinema.entity.account.Account;
import com.example.project_web_cinema.repository.AccountRepository;
import com.example.project_web_cinema.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
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

        String currentEmail = authentication.getName();

        try {
            UserDTO userProfile = userService.getProfileByEmail(currentEmail);
            if (userProfile.getDanhSachVe() == null) {
                userProfile.setDanhSachVe(new ArrayList<>());
            }
            model.addAttribute("user", userProfile);
        } catch (Exception e) {
            UserDTO fallbackUser = UserDTO.builder()
                    .hoTen("Thành Viên All Star")
                    .email(currentEmail)
                    .vaiTro("User")
                    .loaiTaiKhoan("Thuong")
                    .trangThai("HoatDong")
                    .danhSachVe(new ArrayList<>())
                    .build();
            model.addAttribute("user", fallbackUser);
        }
        return "user/profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(Authentication authentication,
                                @RequestParam(value = "hoTen", required = false) String hoTen,
                                @RequestParam(value = "soDienThoai", required = false) String soDienThoai,
                                @RequestParam(value = "ngaySinh", required = false)
                                @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate ngaySinh) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String currentEmail = authentication.getName();

        try {
            Account account = accountRepository.findByEmail(currentEmail)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản: " + currentEmail));

            if (hoTen != null && !hoTen.trim().isEmpty()) {
                account.setHoTen(hoTen);
            }

            if (soDienThoai != null) {
                account.setSoDienThoai(soDienThoai);
            }

            if (ngaySinh != null) {
                account.setNgaySinh(ngaySinh);
            }

            accountRepository.save(account);

        } catch (Exception e) {

            e.printStackTrace();
            return "redirect:/profile?error";
        }

        return "redirect:/profile?success";
    }
}