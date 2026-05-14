package com.example.project_web_cinema.controller;

import com.example.project_web_cinema.dto.RegisterDTO;
import com.example.project_web_cinema.entity.account.Account;
import com.example.project_web_cinema.entity.account.VaiTro;
import com.example.project_web_cinema.repository.AccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerDTO", new RegisterDTO());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("registerDTO") RegisterDTO dto, Model model) {
        if (accountRepository.existsByEmail(dto.getEmail())) {
            model.addAttribute("error", "Email này đã được sử dụng!");
            return "register";
        }

        Account account = Account.builder()
                .hoTen(dto.getHoTen())
                .email(dto.getEmail())
                .soDienThoai(dto.getSoDienThoai())
                .ngaySinh(dto.getNgaySinh())
                .matKhau(passwordEncoder.encode(dto.getMatKhau()))
                .vaiTro(VaiTro.User)
                .build();

        accountRepository.save(account);
        return "redirect:/login?success";
    }
}
