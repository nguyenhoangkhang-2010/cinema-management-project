package com.example.project_web_cinema.repository;

import com.example.project_web_cinema.entity.booktickets.BookTickets;
import com.example.project_web_cinema.entity.booktickets.TrangThaiDatVe;
import com.example.project_web_cinema.entity.pay.TrangThaiThanhToan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<BookTickets, Integer> {

        @Query("SELECT DISTINCT b FROM BookTickets b " +
                        "LEFT JOIN b.account a " +
                        "LEFT JOIN b.dsVe v " +
                        "LEFT JOIN v.movieScreening ms " +
                        "LEFT JOIN ms.movie m " +
                        "LEFT JOIN b.dsThanhToan p " +
                        "WHERE (:search IS NULL OR CAST(b.maDatVe AS string) LIKE %:search% OR a.hoTen LIKE %:search% OR a.email LIKE %:search% OR m.tenPhim LIKE %:search%) "
                        +
                        "AND (:statusOrder IS NULL OR b.trangThaiDatVe = :statusOrder) " +
                        "AND (:statusPayment IS NULL OR p.trangThai = :statusPayment)")
        Page<BookTickets> findBookingsWithFilters(
                        @Param("search") String search,
                        @Param("statusOrder") TrangThaiDatVe statusOrder,
                        @Param("statusPayment") TrangThaiThanhToan statusPayment,
                        Pageable pageable);

        @Modifying
        @Query(value = "INSERT INTO DATVE (MaTaiKhoan, NgayDat, TrangThai, TongTien, MaKhuyenMai) VALUES (:maTaiKhoan, NOW(), :trangThai, :tongTien, :maKhuyenMai)", nativeQuery = true)
        void insertBooking(@Param("maTaiKhoan") Integer maTaiKhoan, @Param("trangThai") String trangThai,
                        @Param("tongTien") Double tongTien, @Param("maKhuyenMai") Integer maKhuyenMai);

        @Query(value = "SELECT LAST_INSERT_ID()", nativeQuery = true)
        Integer getLastInsertId();
}
