package com.example.project_web_cinema.service.admin;

import com.example.project_web_cinema.dto.admin.ShowtimeRequestDTO;
import com.example.project_web_cinema.dto.admin.ShowtimeResponseDTO;
import com.example.project_web_cinema.entity.moviescreening.MovieScreening;
import com.example.project_web_cinema.entity.movie.Movie;
import com.example.project_web_cinema.entity.movietheater.Room;
import com.example.project_web_cinema.repository.MovieScreeningRepository;
import com.example.project_web_cinema.repository.MovieRepository;
import com.example.project_web_cinema.repository.RoomRepository;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AdminShowtimeService {
    private final MovieScreeningRepository movieScreeningRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;
    private final EntityManager em;

    public AdminShowtimeService(MovieScreeningRepository movieScreeningRepository, MovieRepository movieRepository,
            RoomRepository roomRepository, EntityManager em) {
        this.movieScreeningRepository = movieScreeningRepository;
        this.movieRepository = movieRepository;
        this.roomRepository = roomRepository;
        this.em = em;
    }

    public Page<ShowtimeResponseDTO> searchShowtimes(String search, LocalDate ngayChieu, Pageable pageable) {
        return movieScreeningRepository.findShowtimesWithFilters(search, ngayChieu, pageable)
                .map(this::convertToDTO);
    }

    public void addShowtime(ShowtimeRequestDTO dto) {
        validateShowtime(dto, null);
        Movie movie = movieRepository.findById(dto.getMaPhim())
                .orElseThrow(() -> new RuntimeException("Phim không tồn tại"));
        Room room = roomRepository.findById(dto.getMaPhong())
                .orElseThrow(() -> new RuntimeException("Phòng không tồn tại"));

        MovieScreening screening = MovieScreening.builder()
                .movie(movie)
                .room(room)
                .ngayChieu(dto.getNgayChieu())
                .gioBatDau(dto.getGioBatDau())
                .gioKetThuc(dto.getGioKetThuc())
                .giaVe(dto.getGiaVe())
                .build();
        movieScreeningRepository.save(screening);
    }

    public void updateShowtime(Integer id, ShowtimeRequestDTO dto) {
        validateShowtime(dto, id);
        MovieScreening screening = movieScreeningRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Suất chiếu không tồn tại"));
        screening.setMovie(movieRepository.findById(dto.getMaPhim()).orElseThrow());
        screening.setRoom(roomRepository.findById(dto.getMaPhong()).orElseThrow());
        screening.setNgayChieu(dto.getNgayChieu());
        screening.setGioBatDau(dto.getGioBatDau());
        screening.setGioKetThuc(dto.getGioKetThuc());
        screening.setGiaVe(dto.getGiaVe());
        movieScreeningRepository.save(screening);
    }

    public void deleteShowtime(Integer id) {
        // Kiểm tra xem đã có vé đặt cho suất chiếu này chưa
        Long ticketCount = em
                .createQuery("SELECT COUNT(t) FROM Tickets t WHERE t.movieScreening.maSuatChieu = :id", Long.class)
                .setParameter("id", id).getSingleResult();
        if (ticketCount > 0) {
            throw new RuntimeException("Không thể xóa suất chiếu đã có vé được đặt.");
        }
        movieScreeningRepository.deleteById(id);
    }

    public ShowtimeRequestDTO getShowtimeRequestDTO(Integer id) {
        MovieScreening s = movieScreeningRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy"));
        ShowtimeRequestDTO dto = new ShowtimeRequestDTO();
        dto.setMaPhim(s.getMovie().getMaPhim());
        dto.setMaPhong(s.getRoom().getMaPhong());
        dto.setNgayChieu(s.getNgayChieu());
        dto.setGioBatDau(s.getGioBatDau());
        dto.setGioKetThuc(s.getGioKetThuc());
        dto.setGiaVe(s.getGiaVe());
        return dto;
    }

    private void validateShowtime(ShowtimeRequestDTO dto, Integer excludeId) {
        if (!dto.getGioKetThuc().isAfter(dto.getGioBatDau()))
            throw new RuntimeException("Giờ kết thúc phải lớn hơn giờ bắt đầu");
        long overlap = movieScreeningRepository.countOverlappingShowtimes(dto.getMaPhong(), dto.getNgayChieu(),
                dto.getGioBatDau(), dto.getGioKetThuc(), excludeId);
        if (overlap > 0)
            throw new RuntimeException("Phòng chiếu đã có lịch trong khung giờ này");
    }

    private ShowtimeResponseDTO convertToDTO(MovieScreening entity) {
        return ShowtimeResponseDTO.builder().maSuatChieu(entity.getMaSuatChieu())
                .tenPhim(entity.getMovie() != null ? entity.getMovie().getTenPhim() : "N/A")
                .tenPhong(entity.getRoom() != null ? entity.getRoom().getTenPhong() : "N/A")
                .ngayChieu(entity.getNgayChieu()).gioBatDau(entity.getGioBatDau()).gioKetThuc(entity.getGioKetThuc())
                .giaVe(entity.getGiaVe()).build();
    }
}