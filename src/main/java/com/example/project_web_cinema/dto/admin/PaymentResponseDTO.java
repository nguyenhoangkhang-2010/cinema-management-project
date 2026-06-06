package com.example.project_web_cinema.dto.admin;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PaymentResponseDTO {
    private Integer maThanhToan;
    private Integer maDatVe;
    private String hoTenKhachHang;
    private BigDecimal soTien;
    private String phuongThuc;
    private String trangThai;
    private LocalDateTime ngayThanhToan;
}