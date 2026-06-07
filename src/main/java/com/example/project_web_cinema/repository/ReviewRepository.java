package com.example.project_web_cinema.repository;

import com.example.project_web_cinema.entity.rate.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Rate, Integer> {
    @Query("SELECT AVG(r.soSao), COUNT(r) FROM Rate r WHERE r.movie.maPhim = :movieId")
    List<Object[]> getRatingStatsByMovieId(@Param("movieId") Integer movieId);
}