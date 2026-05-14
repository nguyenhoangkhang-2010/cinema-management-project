package com.example.project_web_cinema.entity.pay;

import com.example.project_web_cinema.entity.booktickets.BookTickets;
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
@Table(name = "THANHTOAN")
public class Pay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaThanhToan")
    private Integer maThanhToan;
    @NotNull
    @OneToOne
    @JoinColumn(name = "MaDatVe", nullable = false)
    private BookTickets bookTickets;
    @NotNull
    @DecimalMin("0.00")
    @Column(name = "SoTien", nullable = false, precision = 10, scale = 2)
    private BigDecimal soTien;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "PhuongThuc", nullable = false)
    private PhuongThucThanhToan phuongThuc;
    @Enumerated(EnumType.STRING)
    @Column(name = "TrangThai")
    @Builder.Default
    private TrangThaiThanhToan trangThai = TrangThaiThanhToan.DangXuLy;
    @Column(name = "NgayThanhToan")
    @Builder.Default
    private LocalDateTime ngayThanhToan = LocalDateTime.now();
}
