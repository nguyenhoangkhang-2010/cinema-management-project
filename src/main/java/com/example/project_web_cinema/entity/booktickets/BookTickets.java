package com.example.project_web_cinema.entity.booktickets;

import com.example.project_web_cinema.entity.account.Account;
import com.example.project_web_cinema.entity.promotion.Promotion;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "DATVE")
public class BookTickets {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maDatVe;
    @ManyToOne
    @JoinColumn(name = "MaTaiKhoan", nullable = false)
    private Account account;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaKhuyenMai")
    private Promotion promotion;
    @Builder.Default
    @Column(name = "NgayDat")
    private LocalDateTime ngayDat = LocalDateTime.now();
    @NotNull
    @DecimalMin("0.00")
    @Column(name = "TongTien", nullable = false, precision = 10, scale = 2)
    private BigDecimal tongTien;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "TrangThai")
    private TrangThaiDatVe trangThaiDatVe = TrangThaiDatVe.ChoThanhToan;
}
