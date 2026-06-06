package com.example.project_web_cinema.repository;

import com.example.project_web_cinema.entity.moviescreening.MovieScreening;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;

public interface MovieScreeningRepository extends JpaRepository<MovieScreening, Integer> {

    // Lọc theo Tên phim và Ngày chiếu
    @Query("SELECT s FROM MovieScreening s LEFT JOIN s.movie m LEFT JOIN s.room r " +
            "WHERE (:search IS NULL OR m.tenPhim LIKE %:search%) " +
            "AND (:ngayChieu IS NULL OR s.ngayChieu = :ngayChieu)")
    Page<MovieScreening> findShowtimesWithFilters(@Param("search") String search,
            @Param("ngayChieu") LocalDate ngayChieu, Pageable pageable);

    // Kiểm tra trùng lặp lịch chiếu trong cùng một phòng (Thời gian giao nhau)
    @Query("SELECT COUNT(s) FROM MovieScreening s " +
            "WHERE s.room.maPhong = :roomId AND s.ngayChieu = :date " +
            "AND (:excludeId IS NULL OR s.maSuatChieu != :excludeId) " +
            "AND (s.gioBatDau < :endTime AND s.gioKetThuc > :startTime)")
    long countOverlappingShowtimes(@Param("roomId") Integer roomId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("excludeId") Integer excludeId);
}