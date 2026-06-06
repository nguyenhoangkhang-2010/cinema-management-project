package com.example.project_web_cinema.controller.admin;

import com.example.project_web_cinema.repository.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MovieStatusScheduler {

    private static final Logger logger = LoggerFactory.getLogger(MovieStatusScheduler.class);
    private final MovieRepository movieRepository;

    public MovieStatusScheduler(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void checkAndUpdateMovieStatus() {
        logger.info("Đang kiểm tra và cập nhật các phim đã hết hạn chiếu sang trạng thái Online...");

        int updatedCount = movieRepository.updateExpiredMoviesToOnline();

        logger.info("Đã cập nhật thành công {} phim sang trạng thái Online.", updatedCount);
    }
}