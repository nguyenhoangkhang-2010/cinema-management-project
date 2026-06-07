package com.example.project_web_cinema.service.admin;

import com.example.project_web_cinema.dto.admin.CinemaDTO;
import com.example.project_web_cinema.entity.cinema.Cinema;
import com.example.project_web_cinema.repository.CinemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminCinemaServiceImpl implements AdminCinemaService {

    private final CinemaRepository cinemaRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<CinemaDTO> searchCinemas(String keyword, Pageable pageable) {
        Page<Cinema> cinemaPage;
        if (keyword != null && !keyword.trim().isEmpty()) {
            cinemaPage = cinemaRepository.findByTenRapContainingIgnoreCase(keyword.trim(), pageable);
        } else {
            cinemaPage = cinemaRepository.findAll(pageable);
        }

        return cinemaPage.map(this::mapToDTO);
    }

    @Override
    @Transactional
    public CinemaDTO saveCinema(CinemaDTO cinemaDTO) {
        Cinema cinema;
        if (cinemaDTO.getMaRap() != null) {
            cinema = cinemaRepository.findById(cinemaDTO.getMaRap())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy rạp phim!"));
        } else {
            if (cinemaRepository.existsByTenRapIgnoreCase(cinemaDTO.getTenRap())) {
                throw new RuntimeException("Tên rạp phim đã tồn tại!");
            }
            cinema = new Cinema();
        }

        cinema.setTenRap(cinemaDTO.getTenRap());
        cinema.setDiaChi(cinemaDTO.getDiaChi());
        cinema.setSoDienThoai(cinemaDTO.getSoDienThoai());

        Cinema savedCinema = cinemaRepository.save(cinema);
        return mapToDTO(savedCinema);
    }

    @Override
    @Transactional
    public void deleteCinema(Integer id) {
        cinemaRepository.deleteById(id);
    }

    private CinemaDTO mapToDTO(Cinema cinema) {
        return CinemaDTO.builder()
                .maRap(cinema.getMaRap())
                .tenRap(cinema.getTenRap())
                .diaChi(cinema.getDiaChi())
                .soDienThoai(cinema.getSoDienThoai())
                .soLuongPhong(cinema.getDsPhong() != null ? cinema.getDsPhong().size() : 0)
                .build();
    }
}