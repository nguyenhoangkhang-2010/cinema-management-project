package com.example.project_web_cinema.service;

import com.example.project_web_cinema.dto.user.ReviewRequestDTO;
import com.example.project_web_cinema.dto.user.ReviewResponseDTO;
import com.example.project_web_cinema.entity.account.Account;
import com.example.project_web_cinema.entity.movie.Movie;
import com.example.project_web_cinema.entity.rate.Rate;
import com.example.project_web_cinema.repository.AccountRepository;
import com.example.project_web_cinema.repository.MovieRepository;
import com.example.project_web_cinema.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final AccountRepository accountRepository;
    private final MovieRepository movieRepository;

    @Transactional
    public void saveReview(String email, ReviewRequestDTO request) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản. Vui lòng đăng nhập lại!"));

        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phim!"));

        Rate rate = reviewRepository.findByAccount_EmailAndMovie_MaPhim(email, request.getMovieId())
                .orElse(new Rate());

        rate.setAccount(account);
        rate.setMovie(movie);
        rate.setSoSao(request.getRating());
        rate.setBinhLuan(request.getComment());

        reviewRepository.save(rate);
    }

    public List<ReviewResponseDTO> getReviewsByMovie(Integer movieId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return reviewRepository.findReviewsByMovieId(movieId).stream().map(r -> ReviewResponseDTO.builder()
                .hoTen(r.getAccount().getHoTen())
                .soSao(r.getSoSao())
                .binhLuan(r.getBinhLuan())
                .ngayDanhGia(r.getNgayDanhGia() != null ? r.getNgayDanhGia().format(formatter) : "Gần đây")
                .build()).collect(Collectors.toList());
    }
}