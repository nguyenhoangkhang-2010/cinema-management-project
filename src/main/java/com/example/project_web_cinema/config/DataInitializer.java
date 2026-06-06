package com.example.project_web_cinema.config;

import com.example.project_web_cinema.entity.account.Account;
import com.example.project_web_cinema.entity.account.LoaiTaiKhoan;
import com.example.project_web_cinema.entity.account.TrangThaiTaiKhoan;
import com.example.project_web_cinema.entity.account.VaiTro;
import com.example.project_web_cinema.repository.AccountRepository;
import com.example.project_web_cinema.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(AccountRepository accountRepository, PasswordEncoder passwordEncoder){
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        accountRepository.findByEmail("admin@gmail.com").ifPresentOrElse(
                admin -> {
                    if ("123456".equals(admin.getMatKhau())) {

                        admin.setMatKhau(passwordEncoder.encode("123456"));

                        admin.setCapDo(3);
                        admin.setVaiTro(VaiTro.Admin);
                        admin.setLoaiTaiKhoan(LoaiTaiKhoan.VIP_Pro);
                        admin.setTrangThai(TrangThaiTaiKhoan.HoatDong);

                        accountRepository.save(admin);

                        System.out.println("=========================================================================");
                        System.out.println("DATA INITIALIZER: ĐÃ PHÁT HIỆN ADMIN GỐC TRONG MYSQL!");
                        System.out.println("Đã tự động băm mã hóa mật khẩu '123456' thành chuỗi BCrypt an toàn thành công.");
                        System.out.println("=========================================================================");
                    }
                },
                () -> {
                    Account newAdmin = Account.builder()
                            .hoTen("Tổng Quản Trị Hệ Thống")
                            .ngaySinh(java.time.LocalDate.of(2005, 5, 15))
                            .email("admin@gmail.com")
                            .matKhau(passwordEncoder.encode("123456"))
                            .capDo(3)
                            .vaiTro(VaiTro.Admin)
                            .loaiTaiKhoan(LoaiTaiKhoan.VIP_Pro)
                            .trangThai(TrangThaiTaiKhoan.HoatDong)
                            .build();

                    accountRepository.save(newAdmin);
                    System.out.println("====== ĐÃ TỰ ĐỘNG KHỞI TẠO MỚI TÀI KHOẢN ADMIN (MẬT KHẨU: 123456) ======");
                }
        );
    }
}
