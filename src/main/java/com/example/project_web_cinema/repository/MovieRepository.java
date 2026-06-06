package com.example.project_web_cinema.repository;

import com.example.project_web_cinema.entity.movie.Movie;
import com.example.project_web_cinema.entity.movie.TrangThaiPhim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {
    List<Movie> findByTrangThai(TrangThaiPhim trangThai);

    @Query(value = "SELECT COUNT(ma_phim) FROM phim GROUP BY trang_thai ORDER BY trang_thai", nativeQuery = true)
    List<Long> getMovieCountByStatus();

    @Modifying
    @Transactional
    @Query("UPDATE Movie m SET m.trangThai = com.example.project_web_cinema.entity.movie.TrangThaiPhim.Online WHERE m.ngayKetThucChieu < CURRENT_DATE AND m.trangThai != com.example.project_web_cinema.entity.movie.TrangThaiPhim.Online")
    int updateExpiredMoviesToOnline();
}
