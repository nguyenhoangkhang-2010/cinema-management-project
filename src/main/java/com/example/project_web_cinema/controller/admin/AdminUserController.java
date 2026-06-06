package com.example.project_web_cinema.controller.admin;

import com.example.project_web_cinema.dto.admin.UserResponseDTO;
import com.example.project_web_cinema.entity.account.Account;
import com.example.project_web_cinema.repository.AccountRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    private final AccountRepository accountRepository;

    public AdminUserController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // 1. Hiển thị danh sách khách hàng
    @GetMapping
    public String listUsers(@RequestParam(required = false) String search, Model model) {
        List<UserResponseDTO> users = accountRepository.findAll().stream()
                .filter(a -> search == null || search.isEmpty() ||
                        (a.getHoTen() != null && a.getHoTen().toLowerCase().contains(search.toLowerCase())) ||
                        (a.getEmail() != null && a.getEmail().toLowerCase().contains(search.toLowerCase())))
                .map(a -> UserResponseDTO.builder()
                        .hoTen(a.getHoTen())
                        .email(a.getEmail())
                        .soDienThoai(a.getSoDienThoai())
                        .vaiTro(a.getVaiTro() != null ? a.getVaiTro().toString() : "N/A")
                        .loaiTaiKhoan(a.getLoaiTaiKhoan() != null ? a.getLoaiTaiKhoan().toString() : "N/A")
                        .trangThai(a.getTrangThai() != null ? a.getTrangThai().toString() : "N/A")
                        .build())
                .collect(Collectors.toList());

        model.addAttribute("users", users);
        model.addAttribute("search", search);
        return "admin/users";
    }

    // 2. Nút bấm khóa / mở khóa khách hàng
    @PostMapping("/toggle-lock")
    public String toggleLockUser(@RequestParam String email) {
        // Chỉ code phần an toàn, việc gọi account.setTrangThai(Enum) cần bạn bổ sung
        // thêm
        // sau khi xác định được tên các Enum tương ứng cho việc khóa.
        Account account = accountRepository.findByEmail(email).orElse(null);
        if (account != null) {
            // Thực hiện setTrangThai tại đây
            // accountRepository.save(account);
        }
        return "redirect:/admin/users";
    }
}