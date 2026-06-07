package com.example.project_web_cinema.repository;

import com.example.project_web_cinema.entity.video.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Integer> {
    @Query("SELECT v.duongDan, v.chatLuong FROM Video v WHERE v.movie.maPhim = :movieId")
    List<Object[]> findVideosByMovieId(@Param("movieId") Integer movieId);
}