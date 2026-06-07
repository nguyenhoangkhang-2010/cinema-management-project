package com.example.project_web_cinema.service;

import com.example.project_web_cinema.dto.user.*;
import com.example.project_web_cinema.entity.movie.Movie;
import com.example.project_web_cinema.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieDetailServiceImpl implements MovieDetailService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final ReviewRepository reviewRepository;
    private final VideoRepository videoRepository;
    private final ShowtimeRepository showtimeRepository;

    @Override
    @Transactional(readOnly = true)
    public MovieDetailResponseDTO getMovieDetail(Integer movieId) {
        // 1. Lấy thông tin cơ bản của Phim
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phim có ID: " + movieId));

        MovieDetailResponseDTO response = new MovieDetailResponseDTO();
        response.setMaPhim(movie.getMaPhim());
        response.setTenPhim(movie.getTenPhim());
        response.setPoster(movie.getPoster());
        response.setTrailer(movie.getTrailer());
        response.setDaoDien(movie.getDaoDien());
        response.setThoiLuong(movie.getThoiLuong());
        response.setNgayKhoiChieu(movie.getNgayKhoiChieu());
        response.setNgayKetThucChieu(movie.getNgayKetThucChieu());
        response.setQuocGia(movie.getQuocGia());
        response.setMoTa(movie.getMoTa());
        response.setDoTuoi(movie.getDoTuoi());
        response.setTrangThai(movie.getTrangThai() != null ? movie.getTrangThai().name() : null);

        // 2. Thể loại
        response.setTheLoai(genreRepository.findGenreNamesByMovieId(movieId));

        // 3. Đánh giá (Tính AVG và Count tự động từ JPQL)
        List<Object[]> reviewStats = reviewRepository.getRatingStatsByMovieId(movieId);
        if (!reviewStats.isEmpty() && reviewStats.get(0)[0] != null) {
            response.setDiemDanhGiaTrungBinh(((Number) reviewStats.get(0)[0]).doubleValue());
            response.setTongSoLuotDanhGia(((Number) reviewStats.get(0)[1]).longValue());
        } else {
            response.setDiemDanhGiaTrungBinh(0.0);
            response.setTongSoLuotDanhGia(0L);
        }

        // 4. Video Online
        List<Object[]> videos = videoRepository.findVideosByMovieId(movieId);
        if (!videos.isEmpty()) {
            VideoDTO video = new VideoDTO();
            video.setDuongDanVideo((String) videos.get(0)[0]);
            video.setChatLuong((String) videos.get(0)[1]);
            response.setVideo(video);
        }

        // 5. Lịch Chiếu (Lọc suất chiếu chưa diễn ra)
        List<Object[]> schedules = showtimeRepository.findUpcomingSchedulesByMovieId(movieId, LocalDate.now(),
                LocalTime.now());
        List<ScheduleDTO> scheduleDTOs = new ArrayList<>();
        for (Object[] row : schedules) {
            ScheduleDTO dto = new ScheduleDTO();
            dto.setMaSuatChieu((Integer) row[0]);
            dto.setNgayChieu(row[1] != null ? LocalDate.parse(row[1].toString()) : null);
            dto.setGioBatDau(row[2] != null ? LocalTime.parse(row[2].toString()) : null);
            dto.setGioKetThuc(row[3] != null ? LocalTime.parse(row[3].toString()) : null);
            dto.setGiaVe(row[4] != null ? ((Number) row[4]).doubleValue() : null);
            dto.setTenPhong((String) row[5]);
            dto.setTenRap((String) row[6]);
            dto.setDiaChiRap((String) row[7]);
            scheduleDTOs.add(dto);
        }
        response.setLichChieu(scheduleDTOs);

        // 6. Phim Liên Quan (Lấy max 5 phim đang chiếu)
        List<Movie> relatedMovies = movieRepository.findRelatedMovies(movieId, movie.getQuocGia(), PageRequest.of(0, 5))
                .getContent();
        List<RelatedMovieDTO> relatedDTOs = relatedMovies.stream()
                .map(m -> new RelatedMovieDTO(m.getMaPhim(), m.getTenPhim(), m.getPoster())).toList();
        response.setPhimLienQuan(relatedDTOs);

        return response;
    }
}