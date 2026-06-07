package com.example.project_web_cinema.service.admin;

import com.example.project_web_cinema.dto.admin.CinemaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminCinemaService {
    Page<CinemaDTO> searchCinemas(String keyword, Pageable pageable);

    CinemaDTO saveCinema(CinemaDTO cinemaDTO);

    void deleteCinema(Integer id);
}