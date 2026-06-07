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
}