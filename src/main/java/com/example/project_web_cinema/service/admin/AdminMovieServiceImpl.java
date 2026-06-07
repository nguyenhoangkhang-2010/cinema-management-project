package com.example.project_web_cinema.service.admin;

import com.example.project_web_cinema.dto.admin.MovieDTO;
import com.example.project_web_cinema.entity.category.Category;
import com.example.project_web_cinema.entity.movie.Movie;
import com.example.project_web_cinema.repository.GenreRepository;
import com.example.project_web_cinema.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminMovieServiceImpl implements AdminMovieService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;

    @Override
    @Transactional
    public void saveOrUpdateMovie(MovieDTO dto) {
        Movie movie;
        if (dto.getMaPhim() != null) {
            movie = movieRepository.findById(dto.getMaPhim())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy phim!"));
        } else {
            movie = new Movie();
        }

        // Map dữ liệu cơ bản
        movie.setTenPhim(dto.getTenPhim());
        movie.setThoiLuong(dto.getThoiLuong());
        movie.setNgayKhoiChieu(dto.getNgayKhoiChieu());
        movie.setNgayKetThucChieu(dto.getNgayKetThucChieu());
        movie.setTrangThai(dto.getTrangThai());
        movie.setPoster(dto.getPoster());
        movie.setTrailer(dto.getTrailer());
        movie.setDaoDien(dto.getDaoDien());
        movie.setQuocGia(dto.getQuocGia());
        movie.setMoTa(dto.getMoTa());
        movie.setDoTuoi(dto.getDoTuoi() != null ? dto.getDoTuoi() : 0);
        movie.setCapDoYeuCau(1);

        // Logic kiểm tra ngày chiếu chuyển từ Controller sang
        if (movie.getTrangThai() == com.example.project_web_cinema.entity.movie.TrangThaiPhim.Online) {
            if (movie.getNgayKhoiChieu() == null)
                movie.setNgayKhoiChieu(java.time.LocalDate.now());
            movie.setNgayKetThucChieu(null);
        } else {
            if (movie.getNgayKhoiChieu() != null && movie.getNgayKetThucChieu() == null) {
                movie.setNgayKetThucChieu(movie.getNgayKhoiChieu().plusMonths(2));
            }
        }

        // XỬ LÝ QUAN HỆ MANY-TO-MANY VỚI THỂ LOẠI
        if (dto.getCategoryIds() != null && !dto.getCategoryIds().isEmpty()) {
            List<Category> categories = genreRepository.findAllById(dto.getCategoryIds());
            movie.setDsTheLoai(categories); // JPA sẽ tự động INSERT/DELETE bảng PHIM_THELOAI
        } else {
            movie.setDsTheLoai(new ArrayList<>());
        }

        movieRepository.save(movie);
    }

    @Override
    @Transactional(readOnly = true)
    public MovieDTO getMovieById(Integer id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phim có ID: " + id));
        MovieDTO dto = new MovieDTO();
        dto.setMaPhim(movie.getMaPhim());
        dto.setTenPhim(movie.getTenPhim());
        dto.setPoster(movie.getPoster());
        dto.setTrailer(movie.getTrailer());
        dto.setDaoDien(movie.getDaoDien());
        dto.setThoiLuong(movie.getThoiLuong());
        dto.setNgayKhoiChieu(movie.getNgayKhoiChieu());
        dto.setNgayKetThucChieu(movie.getNgayKetThucChieu());
        dto.setQuocGia(movie.getQuocGia());
        dto.setMoTa(movie.getMoTa());
        dto.setDoTuoi(movie.getDoTuoi());
        dto.setTrangThai(movie.getTrangThai());

        // Lấy danh sách ID thể loại hiện tại của phim để map lên giao diện Edit
        if (movie.getDsTheLoai() != null) {
            dto.setCategoryIds(movie.getDsTheLoai().stream().map(Category::getMaTheLoai).collect(Collectors.toList()));
        }
        return dto;
    }
}