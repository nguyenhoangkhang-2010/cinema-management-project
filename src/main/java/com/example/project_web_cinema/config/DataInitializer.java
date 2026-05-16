package com.example.project_web_cinema.config;

import com.example.project_web_cinema.entity.account.Account;
import com.example.project_web_cinema.repository.AccountRepository;
import com.example.project_web_cinema.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {
    private final AccountRepository accountRepository;
    private final UserService userService;

    public DataInitializer(AccountRepository accountRepository, UserService userService){
        this.accountRepository = accountRepository;
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!accountRepository.existsByEmail("admin@gmail.com")) {

            Account admin = new Account();
            admin.setHoTen("Admin");
            admin.setNgaySinh(LocalDate.of(2005, 5, 15));
            admin.setEmail("admin@gmail.com");

            admin.setMatKhau("123456admin");

            userService.registerNewAccount(admin);

            System.out.println("====== HỆ THỐNG ĐÃ TỰ ĐỘNG KHỞI TẠO TÀI KHOẢN ADMIN VÀ BĂM MẬT KHẨU THÀNH CÔNG! ======");
        }
    }
}
