package com.example.project_web_cinema.service.admin;

import com.example.project_web_cinema.dto.admin.RoomDTO;
import com.example.project_web_cinema.entity.cinema.Cinema;
import com.example.project_web_cinema.entity.movietheater.Room;
import com.example.project_web_cinema.repository.CinemaRepository;
import com.example.project_web_cinema.repository.RoomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AdminRoomService {
    private final RoomRepository roomRepository;
    private final CinemaRepository cinemaRepository;

    public AdminRoomService(RoomRepository roomRepository, CinemaRepository cinemaRepository) {
        this.roomRepository = roomRepository;
        this.cinemaRepository = cinemaRepository;
    }

    public Page<RoomDTO> searchRooms(String search, Pageable pageable) {
        return roomRepository.searchRooms(search, pageable).map(this::convertToDTO);
    }

    public void saveRoom(RoomDTO dto) {
        Room room = new Room();
        if (dto.getMaPhong() != null) {
            room = roomRepository.findById(dto.getMaPhong()).orElse(new Room());
        }
        room.setTenPhong(dto.getTenPhong());
        room.setSoLuongGhe(dto.getSoLuongGhe());

        if (dto.getMaRap() != null) {
            Cinema cinema = cinemaRepository.findById(dto.getMaRap())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy rạp phim"));
            room.setCinema(cinema);
        }
        roomRepository.save(room);
    }

    public void deleteRoom(Integer id) {
        roomRepository.deleteById(id);
    }

    private RoomDTO convertToDTO(Room room) {
        return RoomDTO.builder()
                .maPhong(room.getMaPhong())
                .tenPhong(room.getTenPhong())
                .soLuongGhe(room.getSoLuongGhe())
                .maRap(room.getCinema() != null ? room.getCinema().getMaRap() : null)
                .tenRap(room.getCinema() != null ? room.getCinema().getTenRap() : "N/A")
                .build();
    }
}