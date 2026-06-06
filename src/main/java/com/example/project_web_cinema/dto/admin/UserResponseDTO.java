package com.example.project_web_cinema.dto.admin;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDTO {
    // Sử dụng Email làm key định danh để toggle do không đoán field ID của Account
    private String hoTen;
    private String email;
    private String soDienThoai;
    private String vaiTro;
    private String loaiTaiKhoan;
    private String trangThai;
}