package com.example.project_web_cinema.service.admin;

import com.example.project_web_cinema.dto.admin.RoomDTO;
import com.example.project_web_cinema.entity.cinema.Cinema;
import com.example.project_web_cinema.entity.movietheater.Room;
import com.example.project_web_cinema.repository.CinemaRepository;
import com.example.project_web_cinema.repository.RoomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminRoomService {
    private final RoomRepository roomRepository;
    private final CinemaRepository cinemaRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public AdminRoomService(RoomRepository roomRepository, CinemaRepository cinemaRepository) {
        this.roomRepository = roomRepository;
        this.cinemaRepository = cinemaRepository;
    }

    public Page<RoomDTO> searchRooms(String search, Pageable pageable) {
        return roomRepository.searchRooms(search, pageable).map(this::convertToDTO);
    }

    @Transactional
    public void saveRoom(RoomDTO dto) {
        Room room = new Room();
        boolean isNew = true;
        if (dto.getMaPhong() != null) {
            room = roomRepository.findById(dto.getMaPhong()).orElse(new Room());
            isNew = false;
        }
        room.setTenPhong(dto.getTenPhong());
        room.setSoLuongGhe(dto.getSoLuongGhe());

        if (dto.getMaRap() != null) {
            Cinema cinema = cinemaRepository.findById(dto.getMaRap())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy rạp phim"));
            room.setCinema(cinema);
        }
        room = roomRepository.save(room);

        // NẾU THÊM MỚI PHÒNG CHIẾU -> TỰ ĐỘNG SINH SƠ ĐỒ GHẾ VÀO DATABASE
        if (isNew) {
            // 1. Đảm bảo bảng LOAIGHE có dữ liệu mặc định (Tránh lỗi khóa ngoại)
            entityManager.createNativeQuery(
                    "INSERT IGNORE INTO LOAIGHE (MaLoaiGhe, TenLoaiGhe, GiaPhuThu) VALUES (1, 'Thường', 0), (2, 'VIP', 10000)")
                    .executeUpdate();

            // 2. Sinh tự động số ghế (Mỗi hàng 10 ghế: A1..A10, B1..B10...)
            int totalSeats = dto.getSoLuongGhe();
            int cols = 10;
            int rows = (int) Math.ceil((double) totalSeats / cols);
            char rowChar = 'A';
            int currentCount = 0;

            for (int i = 0; i < rows; i++) {
                for (int j = 1; j <= cols; j++) {
                    if (currentCount >= totalSeats)
                        break;
                    String seatName = String.valueOf((char) (rowChar + i)) + j;

                    // Mặc định cho 2 hàng giữa là ghế VIP (Loại 2), còn lại là Thường (Loại 1)
                    int loaiGhe = (i == rows / 2 || i == (rows / 2) - 1) ? 2 : 1;

                    entityManager.createNativeQuery("INSERT INTO GHE (MaLoaiGhe, SoGhe, MaPhong) VALUES (?, ?, ?)")
                            .setParameter(1, loaiGhe)
                            .setParameter(2, seatName)
                            .setParameter(3, room.getMaPhong())
                            .executeUpdate();
                    currentCount++;
                }
            }
        }
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