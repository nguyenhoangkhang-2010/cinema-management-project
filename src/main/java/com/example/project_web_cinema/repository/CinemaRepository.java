package com.example.project_web_cinema.repository;

import com.example.project_web_cinema.entity.cinema.Cinema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CinemaRepository extends JpaRepository<Cinema, Integer> {
    Page<Cinema> findByTenRapContainingIgnoreCase(String tenRap, Pageable pageable);

    boolean existsByTenRapIgnoreCase(String tenRap);
}