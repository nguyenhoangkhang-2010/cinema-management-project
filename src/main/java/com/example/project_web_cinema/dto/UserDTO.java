package com.example.project_web_cinema.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private String hoTen;
    private LocalDate ngaySinh;
    private String email;
    private String soDienThoai;
    private LocalDateTime ngayTao;
    private int capDo;
    private String vaiTro;
    private String loaiTaiKhoan;
    private String trangThai;
}
