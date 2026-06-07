package com.example.project_web_cinema.repository;

import com.example.project_web_cinema.entity.moviescreening.MovieScreening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ShowtimeRepository extends JpaRepository<MovieScreening, Integer> {
    // Join qua Suất Chiếu -> Phòng -> Rạp
    @Query("SELECT s.maSuatChieu, s.ngayChieu, s.gioBatDau, s.gioKetThuc, s.giaVe, r.tenPhong, c.tenRap, c.diaChi " +
            "FROM MovieScreening s JOIN s.room r JOIN r.cinema c " +
            "WHERE s.movie.maPhim = :movieId AND (s.ngayChieu > :currentDate OR (s.ngayChieu = :currentDate AND s.gioBatDau > :currentTime)) "
            +
            "ORDER BY s.ngayChieu ASC, s.gioBatDau ASC")
    List<Object[]> findUpcomingSchedulesByMovieId(@Param("movieId") Integer movieId,
            @Param("currentDate") LocalDate currentDate, @Param("currentTime") LocalTime currentTime);

    // --- QUERIES CHO MUA VÉ NHANH ---
    @Query("SELECT m.maPhim, m.tenPhim FROM Movie m WHERE m.trangThai = com.example.project_web_cinema.entity.movie.TrangThaiPhim.DangChieu")
    List<Object[]> findMoviesForQuickBooking();

    @Query("SELECT DISTINCT c.maRap, c.tenRap FROM MovieScreening s JOIN s.room r JOIN r.cinema c WHERE s.movie.maPhim = :movieId AND s.ngayChieu >= CURRENT_DATE")
    List<Object[]> findCinemasForQuickBooking(@Param("movieId") Integer movieId);

    @Query("SELECT DISTINCT s.ngayChieu FROM MovieScreening s JOIN s.room r JOIN r.cinema c WHERE s.movie.maPhim = :movieId AND c.maRap = :cinemaId AND s.ngayChieu >= CURRENT_DATE ORDER BY s.ngayChieu ASC")
    List<LocalDate> findDatesForQuickBooking(@Param("movieId") Integer movieId, @Param("cinemaId") Integer cinemaId);

    @Query("SELECT s.maSuatChieu, s.gioBatDau FROM MovieScreening s JOIN s.room r JOIN r.cinema c WHERE s.movie.maPhim = :movieId AND c.maRap = :cinemaId AND s.ngayChieu = :date AND (s.ngayChieu > CURRENT_DATE OR s.gioBatDau > CURRENT_TIME) ORDER BY s.gioBatDau ASC")
    List<Object[]> findShowtimesForQuickBooking(@Param("movieId") Integer movieId, @Param("cinemaId") Integer cinemaId,
            @Param("date") LocalDate date);

    // --- QUERIES CHO TRANG THANH TOÁN (CHECKOUT) ---
    @Query(value = "SELECT p.TenPhim, p.Poster, r.TenRap, pc.TenPhong, sc.NgayChieu, sc.GioBatDau, sc.GiaVe, p.DoTuoi "
            +
            "FROM SUATCHIEU sc " +
            "JOIN PHIM p ON sc.MaPhim = p.MaPhim " +
            "JOIN PHONGCHIEU pc ON sc.MaPhong = pc.MaPhong " +
            "JOIN RAPPHIM r ON pc.MaRap = r.MaRap " +
            "WHERE sc.MaSuatChieu = :showtimeId", nativeQuery = true)
    List<Object[]> getCheckoutShowtimeInfo(@Param("showtimeId") Integer showtimeId);

    @Query(value = "SELECT g.MaGhe, g.SoGhe, lg.TenLoaiGhe, lg.GiaPhuThu " +
            "FROM GHE g " +
            "JOIN LOAIGHE lg ON g.MaLoaiGhe = lg.MaLoaiGhe " +
            "WHERE g.MaGhe IN :seatIds", nativeQuery = true)
    List<Object[]> getCheckoutSeatsInfo(@Param("seatIds") List<Integer> seatIds);

    // --- QUERIES CHO TRANG CHỌN GHẾ (SEAT SELECTION) ---
    @Query(value = "SELECT p.TenPhim, p.Poster, sc.NgayChieu, sc.GioBatDau, pc.TenPhong, r.TenRap, sc.GiaVe " +
            "FROM SUATCHIEU sc " +
            "JOIN PHIM p ON sc.MaPhim = p.MaPhim " +
            "JOIN PHONGCHIEU pc ON sc.MaPhong = pc.MaPhong " +
            "JOIN RAPPHIM r ON pc.MaRap = r.MaRap " +
            "WHERE sc.MaSuatChieu = :showtimeId", nativeQuery = true)
    List<Object[]> getShowtimeInfoForBooking(@Param("showtimeId") Integer showtimeId);

    @Query(value = "SELECT g.MaGhe, g.SoGhe, lg.TenLoaiGhe, lg.GiaPhuThu, " +
            "(SELECT COUNT(*) FROM VE v JOIN DATVE dv ON v.MaDatVe = dv.MaDatVe WHERE v.MaGhe = g.MaGhe AND v.MaSuatChieu = :showtimeId AND dv.TrangThai != 'DaHuy') as DaDat "
            +
            "FROM GHE g " +
            "JOIN LOAIGHE lg ON g.MaLoaiGhe = lg.MaLoaiGhe " +
            "JOIN SUATCHIEU sc ON g.MaPhong = sc.MaPhong " +
            "WHERE sc.MaSuatChieu = :showtimeId", nativeQuery = true)
    List<Object[]> getSeatsForBooking(@Param("showtimeId") Integer showtimeId);

    // ==========================================
    // QUERIES CHO BOOKING FLOW (REST API)
    // ==========================================
    @Query(value = "SELECT DISTINCT c.MaRap, c.TenRap FROM SUATCHIEU s JOIN PHONGCHIEU r ON s.MaPhong = r.MaPhong JOIN RAPPHIM c ON r.MaRap = c.MaRap WHERE s.MaPhim = :movieId", nativeQuery = true)
    List<Object[]> getCinemasByMovieId(@Param("movieId") Integer movieId);

    @Query(value = "SELECT DISTINCT p.MaPhong, p.TenPhong FROM PHONGCHIEU p JOIN SUATCHIEU s ON p.MaPhong = s.MaPhong WHERE p.MaRap = :cinemaId", nativeQuery = true)
    List<Object[]> getRoomsByCinemaId(@Param("cinemaId") Integer cinemaId);

    @Query(value = "SELECT g.MaGhe, g.SoGhe, lg.TenLoaiGhe, lg.GiaPhuThu, " +
            "(SELECT COUNT(*) FROM VE v JOIN DATVE dv ON v.MaDatVe = dv.MaDatVe WHERE v.MaGhe = g.MaGhe AND v.MaSuatChieu = :showtimeId AND dv.TrangThai != 'DaHuy') as DaDat "
            +
            "FROM GHE g JOIN LOAIGHE lg ON g.MaLoaiGhe = lg.MaLoaiGhe WHERE g.MaPhong = :roomId", nativeQuery = true)
    List<Object[]> getSeatsByRoomAndShowtime(@Param("roomId") Integer roomId, @Param("showtimeId") Integer showtimeId);
}