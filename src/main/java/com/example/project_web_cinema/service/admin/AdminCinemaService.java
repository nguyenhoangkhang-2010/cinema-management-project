package com.example.project_web_cinema.service.admin;

import com.example.project_web_cinema.dto.admin.CinemaDTO;
import com.example.project_web_cinema.entity.cinema.Cinema;
import com.example.project_web_cinema.repository.CinemaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AdminCinemaService {
    private final CinemaRepository cinemaRepository;

    public AdminCinemaService(CinemaRepository cinemaRepository) {
        this.cinemaRepository = cinemaRepository;
    }

    public Page<CinemaDTO> searchCinemas(String search, Pageable pageable) {
        return cinemaRepository.searchCinemas(search, pageable).map(this::convertToDTO);
    }

    public void saveCinema(CinemaDTO dto) {
        Cinema cinema = new Cinema();
        if (dto.getMaRap() != null) {
            cinema = cinemaRepository.findById(dto.getMaRap()).orElse(new Cinema());
        }
        cinema.setTenRap(dto.getTenRap());
        cinema.setDiaChi(dto.getDiaChi());
        cinema.setSoDienThoai(dto.getSoDienThoai());
        cinemaRepository.save(cinema);
    }

    public void deleteCinema(Integer id) {
        cinemaRepository.deleteById(id);
    }

    private CinemaDTO convertToDTO(Cinema c) {
        return CinemaDTO.builder()
                .maRap(c.getMaRap())
                .tenRap(c.getTenRap())
                .diaChi(c.getDiaChi())
                .soDienThoai(c.getSoDienThoai())
                .soLuongPhong(c.getDsPhong() != null ? c.getDsPhong().size() : 0)
                .build();
    }
}