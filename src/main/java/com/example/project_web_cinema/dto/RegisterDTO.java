package com.example.project_web_cinema.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterDTO {
    private String hoTen;
    private String email;
    private String soDienThoai;
    private String matKhau;
    private String nhapLaiMatKhau;
    private LocalDate ngaySinh;
}
