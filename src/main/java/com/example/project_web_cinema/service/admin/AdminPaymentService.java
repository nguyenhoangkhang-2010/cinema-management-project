package com.example.project_web_cinema.service.admin;

import com.example.project_web_cinema.dto.admin.PaymentResponseDTO;
import com.example.project_web_cinema.entity.pay.Pay;
import com.example.project_web_cinema.entity.pay.TrangThaiThanhToan;
import com.example.project_web_cinema.repository.PayRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AdminPaymentService {
    private final PayRepository payRepository;

    public AdminPaymentService(PayRepository payRepository) {
        this.payRepository = payRepository;
    }

    public Page<PaymentResponseDTO> getPayments(String statusStr, Pageable pageable) {
        TrangThaiThanhToan status = null;
        if (statusStr != null && !statusStr.isEmpty()) {
            try {
                status = TrangThaiThanhToan.valueOf(statusStr);
            } catch (Exception ignored) {
            }
        }
        return payRepository.findPaymentsWithFilters(status, pageable).map(this::convertToDTO);
    }

    private PaymentResponseDTO convertToDTO(Pay entity) {
        return PaymentResponseDTO.builder()
                .maThanhToan(entity.getMaThanhToan())
                .maDatVe(entity.getBookTickets() != null ? entity.getBookTickets().getMaDatVe() : null)
                .hoTenKhachHang(entity.getBookTickets() != null && entity.getBookTickets().getAccount() != null
                        ? entity.getBookTickets().getAccount().getHoTen()
                        : "N/A")
                .soTien(entity.getSoTien())
                .phuongThuc(entity.getPhuongThuc() != null ? entity.getPhuongThuc().name() : "N/A")
                .trangThai(entity.getTrangThai() != null ? entity.getTrangThai().name() : "N/A")
                .ngayThanhToan(entity.getNgayThanhToan())
                .build();
    }
}