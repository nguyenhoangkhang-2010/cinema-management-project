package com.example.project_web_cinema.entity.promotion;

import com.example.project_web_cinema.entity.booktickets.BookTickets;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "KHUYENMAI")
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaKhuyenMai")
    private Integer maKhuyenMai;
    @Column(name = "TenKhuyenMai", nullable = false, length = 100)
    private String tenKhuyenMai;
    @Lob
    @Column
    private String moTa;
    @NotNull
    @DecimalMin("0.00")
    @DecimalMax("100.00")
    @Column(name = "PhanTramGiam", nullable = false, precision = 5, scale = 2)
    private BigDecimal phanTramGiam;
    @Column(name = "NgayBatDau", nullable = false)
    private LocalDate ngayBatDau;
    @Column(name = "NgayKetThuc", nullable = false)
    private LocalDate ngayKetThuc;
    @NotNull
    @Min(0)
    @Column(name = "SoLuong", nullable = false)
    private Integer soLuong;
    @Enumerated(EnumType.STRING)
    @Column(name = "LoaiTaiKhoanApDung")
    @Builder.Default
    private LoaiTaiKhoanToiThieu loaiTaiKhoanToiThieu = LoaiTaiKhoanToiThieu.Thuong;
    @Enumerated(EnumType.STRING)
    @Column(name = "TrangThai")
    @Builder.Default
    private TrangThaiKhuyenMai trangThaiKhuyenMai = TrangThaiKhuyenMai.HoatDong;
    @OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL)
    private List<BookTickets> dsDatVe;
    @Column(name = "Poster")
    private String poster;
}
