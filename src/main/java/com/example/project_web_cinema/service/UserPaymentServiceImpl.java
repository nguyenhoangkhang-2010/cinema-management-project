package com.example.project_web_cinema.service;

import com.example.project_web_cinema.dto.user.TicketHistoryDTO;
import com.example.project_web_cinema.dto.BookingHistoryProjection;
import com.example.project_web_cinema.repository.TicketRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserPaymentServiceImpl implements UserPaymentService {

        @PersistenceContext
        private EntityManager entityManager;

        private final TicketRepository ticketRepository;

        @Override
        @Transactional
        public Integer processBooking(String email, Integer showtimeId, List<Integer> seatIds, Double totalPrice) {
                // 1. Lấy MaTaiKhoan của người dùng hiện tại
                Integer maTaiKhoan = (Integer) entityManager
                                .createNativeQuery("SELECT MaTaiKhoan FROM TAIKHOAN WHERE Email = ?")
                                .setParameter(1, email).getSingleResult();

                // 2. Tạo hóa đơn ĐẶT VÉ
                entityManager.createNativeQuery(
                                "INSERT INTO DATVE (MaTaiKhoan, NgayDat, TongTien, TrangThai) VALUES (?, NOW(), ?, 'DaThanhToan')")
                                .setParameter(1, maTaiKhoan).setParameter(2, totalPrice).executeUpdate();

                Integer maDatVe = ((Number) entityManager.createNativeQuery("SELECT LAST_INSERT_ID()")
                                .getSingleResult())
                                .intValue();

                // 3. Gắn từng VÉ cho hóa đơn
                for (Integer seatId : seatIds) {
                        entityManager.createNativeQuery("INSERT INTO VE (MaDatVe, MaSuatChieu, MaGhe) VALUES (?, ?, ?)")
                                        .setParameter(1, maDatVe).setParameter(2, showtimeId).setParameter(3, seatId)
                                        .executeUpdate();
                }

                // 4. Ghi nhận giao dịch THANH TOÁN
                entityManager.createNativeQuery(
                                "INSERT INTO THANHTOAN (MaDatVe, SoTien, PhuongThuc, TrangThai, NgayThanhToan) VALUES (?, ?, 'TienMat', 'ThanhCong', NOW())")
                                .setParameter(1, maDatVe).setParameter(2, totalPrice).executeUpdate();

                return maDatVe;
        }

        @Override
        public List<TicketHistoryDTO> getHistory(String email) {
                List<BookingHistoryProjection> rawTickets = ticketRepository.findTicketHistoryByEmail(email);
                List<TicketHistoryDTO> list = new ArrayList<>();
                if (rawTickets != null) {
                        for (BookingHistoryProjection row : rawTickets) {
                                TicketHistoryDTO dto = new TicketHistoryDTO();
                                dto.setMaDatVe(row.getMaDatVe());
                                dto.setTenPhim(row.getTenPhim());
                                dto.setPoster(row.getPoster());
                                dto.setNgayChieu(row.getNgayChieu());
                                dto.setGioBatDau(row.getGioBatDau());
                                dto.setTenPhong(row.getTenPhong());
                                dto.setTenRap(row.getTenRap());
                                dto.setNgayDat(row.getNgayDat());
                                dto.setTongTien(row.getTongTien());
                                dto.setTrangThai(row.getTrangThai());
                                dto.setDanhSachGhe(row.getDanhSachGhe());
                                list.add(dto);
                        }
                }
                return list;
        }

        @Override
        public boolean isOrderValid(Integer orderId) {
                Number count = (Number) entityManager.createNativeQuery("SELECT COUNT(*) FROM DATVE WHERE MaDatVe = ?")
                                .setParameter(1, orderId)
                                .getSingleResult();
                return count.intValue() > 0;
        }
}