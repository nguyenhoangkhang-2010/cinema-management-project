package com.example.project_web_cinema.entity.account;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name="TAIKHOAN")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maTaiKhoan;

    @Column(name = "HoTen", nullable = false, length = 100)
    private String hoTen;

    @Column(name = "NgaySinh", nullable = false)
    private LocalDate ngaySinh;

    @Column(name = "Email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "SoDienThoai", unique = true, length = 15)
    private String soDienThoai;

    @Column(name = "MatKhau", nullable = false, length = 255)
    private String matKhau;

    @Column(name = "NgayTao")
    private LocalDate ngayTao;

    @Column(name = "CapDo")
    private Integer capDo = 1;

    @Enumerated(EnumType.STRING)
    @Column(name = "VaiTro")
    private VaiTro vaiTro = VaiTro.User;

    @Enumerated(EnumType.STRING)
    @Column(name = "LoaiTaiKhoan")
    private LoaiTaiKhoan loaiTaiKhoan = LoaiTaiKhoan.Thuong;

    @Enumerated(EnumType.STRING)
    @Column(name = "TrangThai")
    private TrangThaiTaiKhoan trangThai = TrangThaiTaiKhoan.HoatDong;

    @PrePersist
    public void prePersist() {
        if (ngayTao == null) {
            ngayTao = LocalDate.now();
        }
    }
}
