package com.example.project_web_cinema.repository;

import com.example.project_web_cinema.entity.tickets.Tickets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Tickets, Integer> {
    @Query(value = "SELECT v.MaVe AS id, p.TenPhim AS tenPhim, pc.TenPhong AS tenPhong, rp.TenRap AS tenRap, " +
            "CONCAT(sc.NgayChieu, ' ', sc.GioBatDau) AS suatChieu, g.SoGhe AS danhSachGhe, dv.TrangThai AS trangThai " +
            "FROM VE v " +
            "JOIN DATVE dv ON v.MaDatVe = dv.MaDatVe " +
            "JOIN TAIKHOAN tk ON dv.MaTaiKhoan = tk.MaTaiKhoan " +
            "JOIN SUATCHIEU sc ON v.MaSuatChieu = sc.MaSuatChieu " +
            "JOIN PHIM p ON sc.MaPhim = p.MaPhim " +
            "JOIN PHONGCHIEU pc ON sc.MaPhong = pc.MaPhong " +
            "JOIN RAPPHIM rp ON pc.MaRap = rp.MaRap " +
            "JOIN GHE g ON v.MaGhe = g.MaGhe " +
            "WHERE tk.Email = :email AND dv.TrangThai != 'DaHuy'", nativeQuery = true)
    List<Object[]> findTicketHistoryByEmail(@Param("email") String email);

    // Đếm tổng số vé đã thanh toán thành công của 1 tài khoản
    @Query("SELECT COUNT(t) FROM Tickets t JOIN t.bookTickets b WHERE b.account.maTaiKhoan = :accountId AND b.trangThaiDatVe = com.example.project_web_cinema.entity.booktickets.TrangThaiDatVe.DaThanhToan")
    Integer countPurchasedTicketsByAccountId(@Param("accountId") Integer accountId);
}
