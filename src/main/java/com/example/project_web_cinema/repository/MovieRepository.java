package com.example.project_web_cinema.repository;

import com.example.project_web_cinema.entity.movie.Movie;
import com.example.project_web_cinema.entity.movie.TrangThaiPhim;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {
        Page<Movie> findByTenPhimContainingIgnoreCaseOrQuocGiaContainingIgnoreCase(String tenPhim, String quocGia,
                        Pageable pageable);

        List<Movie> findByTrangThai(TrangThaiPhim trangThai);

        // Lấy đúng 4 phim theo trạng thái, sắp xếp mới nhất (Tránh kéo toàn bộ DB)
        List<Movie> findTop4ByTrangThaiOrderByNgayKhoiChieuDesc(TrangThaiPhim trangThai);

        // Truy vấn Phim Liên Quan (Loại trừ phim hiện tại, ưu tiên cùng Quốc Gia hoặc
        // Đang Chiếu)
        @Query("SELECT m FROM Movie m WHERE m.maPhim != :movieId AND (m.quocGia = :quocGia OR m.trangThai = com.example.project_web_cinema.entity.movie.TrangThaiPhim.DangChieu) ORDER BY m.ngayKhoiChieu DESC")
        Page<Movie> findRelatedMovies(@org.springframework.data.repository.query.Param("movieId") Integer movieId,
                        @org.springframework.data.repository.query.Param("quocGia") String quocGia, Pageable pageable);

        @Query(value = "SELECT COUNT(ma_phim) FROM phim GROUP BY trang_thai ORDER BY trang_thai", nativeQuery = true)
        List<Long> getMovieCountByStatus();

        @Modifying
        @Transactional
        @Query("UPDATE Movie m SET m.trangThai = com.example.project_web_cinema.entity.movie.TrangThaiPhim.Online WHERE m.ngayKetThucChieu < CURRENT_DATE AND m.trangThai != com.example.project_web_cinema.entity.movie.TrangThaiPhim.Online")
        int updateExpiredMoviesToOnline();

        // Slider sắp chiếu
        @Query(value = "SELECT MaPhim, TenPhim, Poster, NgayKhoiChieu, Trailer " +
                        "FROM PHIM WHERE TrangThai = :str OR TrangThai = :ord ORDER BY NgayKhoiChieu ASC LIMIT 10", nativeQuery = true)
        List<Object[]> findUpcomingMoviesNative(@org.springframework.data.repository.query.Param("str") String str,
                        @org.springframework.data.repository.query.Param("ord") int ord);

        // Phim đang chiếu nổi bật nhất (Top Rating)
        @Query(value = "SELECT p.MaPhim, p.TenPhim, p.Poster, p.ThoiLuong, " +
                        "COALESCE(AVG(d.SoSao), 0) as avgRating, COUNT(d.MaDanhGia) as countRating " +
                        "FROM PHIM p LEFT JOIN DANHGIA d ON p.MaPhim = d.MaPhim " +
                        "WHERE p.TrangThai = :str OR p.TrangThai = :ord " +
                        "GROUP BY p.MaPhim ORDER BY avgRating DESC LIMIT 6", nativeQuery = true)
        List<Object[]> findTopRatedNowShowingMoviesNative(
                        @org.springframework.data.repository.query.Param("str") String str,
                        @org.springframework.data.repository.query.Param("ord") int ord);

        // Phân trang Star Shop
        @Query(value = "SELECT p.MaPhim, p.TenPhim, p.Poster, p.ThoiLuong, p.QuocGia, " +
                        "COALESCE(AVG(d.SoSao), 0) as avgRating, COUNT(d.MaDanhGia) as countRating " +
                        "FROM PHIM p LEFT JOIN DANHGIA d ON p.MaPhim = d.MaPhim " +
                        "WHERE p.TrangThai = 'DangChieu' GROUP BY p.MaPhim", countQuery = "SELECT COUNT(MaPhim) FROM PHIM WHERE TrangThai = 'DangChieu'", nativeQuery = true)
        Page<Object[]> findNowShowingMoviesNative(Pageable pageable);
}
