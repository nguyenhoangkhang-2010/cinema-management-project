package com.example.project_web_cinema.service;

import com.example.project_web_cinema.dto.user.TicketHistoryDTO;
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

        Integer maDatVe = ((Number) entityManager.createNativeQuery("SELECT LAST_INSERT_ID()").getSingleResult())
                .intValue();

        // 3. Gắn từng VÉ cho hóa đơn
        for (Integer seatId : seatIds) {
            entityManager.createNativeQuery("INSERT INTO VE (MaDatVe, MaSuatChieu, MaGhe) VALUES (?, ?, ?)")
                    .setParameter(1, maDatVe).setParameter(2, showtimeId).setParameter(3, seatId).executeUpdate();
        }

        // 4. Ghi nhận giao dịch THANH TOÁN
        entityManager.createNativeQuery(
                "INSERT INTO THANHTOAN (MaDatVe, SoTien, PhuongThuc, TrangThai, NgayThanhToan) VALUES (?, ?, 'TienMat', 'ThanhCong', NOW())")
                .setParameter(1, maDatVe).setParameter(2, totalPrice).executeUpdate();

        return maDatVe;
    }

    @Override
    public List<TicketHistoryDTO> getHistory(String email) {
        String sql = "SELECT dv.MaDatVe, p.TenPhim, p.Poster, sc.NgayChieu, sc.GioBatDau, pc.TenPhong, r.TenRap, " +
                "dv.NgayDat, dv.TongTien, dv.TrangThai, GROUP_CONCAT(g.SoGhe SEPARATOR ', ') as DanhSachGhe " +
                "FROM DATVE dv JOIN TAIKHOAN tk ON dv.MaTaiKhoan = tk.MaTaiKhoan JOIN VE v ON dv.MaDatVe = v.MaDatVe " +
                "JOIN SUATCHIEU sc ON v.MaSuatChieu = sc.MaSuatChieu JOIN PHONGCHIEU pc ON sc.MaPhong = pc.MaPhong " +
                "JOIN RAPPHIM r ON pc.MaRap = r.MaRap JOIN GHE g ON v.MaGhe = g.MaGhe JOIN PHIM p ON sc.MaPhim = p.MaPhim "
                +
                "WHERE tk.Email = ? GROUP BY dv.MaDatVe ORDER BY dv.NgayDat DESC";

        List<Object[]> rows = entityManager.createNativeQuery(sql).setParameter(1, email).getResultList();
        List<TicketHistoryDTO> list = new ArrayList<>();
        for (Object[] row : rows) {
            TicketHistoryDTO dto = new TicketHistoryDTO();
            dto.setMaDatVe((Integer) row[0]);
            dto.setTenPhim((String) row[1]);
            dto.setPoster((String) row[2]);
            dto.setNgayChieu(row[3] != null ? row[3].toString() : "");
            dto.setGioBatDau(row[4] != null ? row[4].toString() : "");
            dto.setTenPhong((String) row[5]);
            dto.setTenRap((String) row[6]);
            dto.setNgayDat(row[7] != null ? row[7].toString() : "");
            dto.setTongTien(row[8] != null ? ((Number) row[8]).doubleValue() : 0.0);
            dto.setTrangThai((String) row[9]);
            dto.setDanhSachGhe((String) row[10]);
            list.add(dto);
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