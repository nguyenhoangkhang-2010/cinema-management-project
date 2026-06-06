package com.example.project_web_cinema.repository;

import com.example.project_web_cinema.entity.cinema.Cinema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CinemaRepository extends JpaRepository<Cinema, Integer> {

    @Query("SELECT c FROM Cinema c WHERE (:search IS NULL OR c.tenRap LIKE %:search% OR c.diaChi LIKE %:search%)")
    Page<Cinema> searchCinemas(@Param("search") String search, Pageable pageable);

}