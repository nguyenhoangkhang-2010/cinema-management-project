package com.example.project_web_cinema.repository;

import com.example.project_web_cinema.entity.tickets.Tickets;
import com.example.project_web_cinema.dto.BookingHistoryProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Tickets, Integer> {
    @Query(value = "SELECT dv.MaDatVe AS maDatVe, DATE_FORMAT(dv.NgayDat, '%d/%m/%Y %H:%i') AS ngayDat, dv.TongTien AS tongTien, dv.TrangThai AS trangThai, "
            +
            "p.TenPhim AS tenPhim, p.Poster AS poster, rp.TenRap AS tenRap, pc.TenPhong AS tenPhong, " +
            "TIME_FORMAT(sc.GioBatDau, '%H:%i') AS gioBatDau, DATE_FORMAT(sc.NgayChieu, '%d/%m/%Y') AS ngayChieu, " +
            "(SELECT GROUP_CONCAT(g.SoGhe SEPARATOR ', ') FROM VE v2 JOIN GHE g ON v2.MaGhe = g.MaGhe WHERE v2.MaDatVe = dv.MaDatVe) AS danhSachGhe "
            +
            "FROM DATVE dv " +
            "JOIN TAIKHOAN tk ON dv.MaTaiKhoan = tk.MaTaiKhoan " +
            "JOIN VE v ON v.MaDatVe = dv.MaDatVe " +
            "JOIN SUATCHIEU sc ON v.MaSuatChieu = sc.MaSuatChieu " +
            "JOIN PHIM p ON sc.MaPhim = p.MaPhim " +
            "JOIN PHONGCHIEU pc ON sc.MaPhong = pc.MaPhong " +
            "JOIN RAPPHIM rp ON pc.MaRap = rp.MaRap " +
            "WHERE tk.Email = :email AND dv.TrangThai != 'DaHuy' " +
            "GROUP BY dv.MaDatVe, dv.NgayDat, dv.TongTien, dv.TrangThai, p.TenPhim, p.Poster, rp.TenRap, pc.TenPhong, sc.GioBatDau, sc.NgayChieu "
            +
            "ORDER BY dv.NgayDat DESC", nativeQuery = true)
    List<BookingHistoryProjection> findTicketHistoryByEmail(@Param("email") String email);

    // Đếm tổng số vé đã thanh toán thành công của 1 tài khoản
    @Query("SELECT COUNT(t) FROM Tickets t JOIN t.bookTickets b WHERE b.account.maTaiKhoan = :accountId AND b.trangThaiDatVe = com.example.project_web_cinema.entity.booktickets.TrangThaiDatVe.DaThanhToan")
    Integer countPurchasedTicketsByAccountId(@Param("accountId") Integer accountId);

    @Modifying
    @Query(value = "INSERT INTO VE (MaDatVe, MaSuatChieu, MaGhe) VALUES (:maDatVe, :maSuatChieu, :maGhe)", nativeQuery = true)
    void insertTicket(@Param("maDatVe") Integer maDatVe, @Param("maSuatChieu") Integer maSuatChieu,
            @Param("maGhe") Integer maGhe);
}
