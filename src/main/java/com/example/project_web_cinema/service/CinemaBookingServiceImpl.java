package com.example.project_web_cinema.service;

import com.example.project_web_cinema.dto.user.BookingDTOs;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class CinemaBookingServiceImpl implements CinemaBookingService {

    @PersistenceContext
    private EntityManager entityManager;

    // Bộ nhớ đệm giữ ghế (Key: showtimeId_seatId, Value: Timestamp hết hạn)
    private static final Map<String, Long> seatLocks = new ConcurrentHashMap<>();
    private static final long LOCK_TIME_MS = 5 * 60 * 1000; // 5 phút

    @Override
    public List<BookingDTOs.CinemaResDTO> getCinemasByMovie(Integer movieId) {
        String sql = "SELECT DISTINCT c.MaRap, c.TenRap FROM SUATCHIEU s JOIN PHONGCHIEU r ON s.MaPhong = r.MaPhong JOIN RAPPHIM c ON r.MaRap = c.MaRap WHERE s.MaPhim = ?";
        List<Object[]> rows = entityManager.createNativeQuery(sql).setParameter(1, movieId).getResultList();
        List<BookingDTOs.CinemaResDTO> result = new ArrayList<>();
        for (Object[] r : rows) {
            BookingDTOs.CinemaResDTO dto = new BookingDTOs.CinemaResDTO();
            dto.setId((Integer) r[0]);
            dto.setName((String) r[1]);
            result.add(dto);
        }
        return result;
    }

    @Override
    public List<BookingDTOs.RoomResDTO> getRoomsByShowtime(Integer showtimeId) {
        String sql = "SELECT p.MaPhong, p.TenPhong FROM PHONGCHIEU p JOIN SUATCHIEU s ON p.MaPhong = s.MaPhong WHERE s.MaSuatChieu = ?";
        List<Object[]> rows = entityManager.createNativeQuery(sql).setParameter(1, showtimeId).getResultList();
        List<BookingDTOs.RoomResDTO> result = new ArrayList<>();
        for (Object[] r : rows) {
            BookingDTOs.RoomResDTO dto = new BookingDTOs.RoomResDTO();
            dto.setId((Integer) r[0]);
            dto.setName((String) r[1]);
            result.add(dto);
        }
        return result;
    }

    @Override
    public List<BookingDTOs.SeatResDTO> getSeats(Integer roomId, Integer showtimeId) {
        String sql = "SELECT g.MaGhe, g.SoGhe, lg.TenLoaiGhe, lg.GiaPhuThu, " +
                "(SELECT COUNT(*) FROM VE v JOIN DATVE dv ON v.MaDatVe = dv.MaDatVe WHERE v.MaGhe = g.MaGhe AND v.MaSuatChieu = ? AND dv.TrangThai != 'DaHuy') as DaDat "
                +
                "FROM GHE g JOIN LOAIGHE lg ON g.MaLoaiGhe = lg.MaLoaiGhe WHERE g.MaPhong = ?";

        List<Object[]> rows = entityManager.createNativeQuery(sql).setParameter(1, showtimeId).setParameter(2, roomId)
                .getResultList();
        List<BookingDTOs.SeatResDTO> seats = new ArrayList<>();
        long now = System.currentTimeMillis();

        for (Object[] r : rows) {
            Integer maGhe = (Integer) r[0];
            boolean isBooked = ((Number) r[4]).intValue() > 0;

            // Kiểm tra ghế có đang bị ai đó giữ trong RAM không
            String lockKey = showtimeId + "_" + maGhe;
            Long lockExpiry = seatLocks.get(lockKey);
            boolean isLocked = (!isBooked && lockExpiry != null && lockExpiry > now);

            BookingDTOs.SeatResDTO dto = new BookingDTOs.SeatResDTO();
            dto.setMaGhe(maGhe);
            dto.setSoGhe((String) r[1]);
            dto.setLoaiGhe((String) r[2]);
            dto.setGiaPhuThu(((Number) r[3]).doubleValue());
            dto.setIsBooked(isBooked);
            dto.setIsLocked(isLocked);
            seats.add(dto);
        }
        return seats;
    }

    @Override
    public synchronized boolean lockSeats(BookingDTOs.SelectSeatReqDTO req) {
        long now = System.currentTimeMillis();
        // Kiểm tra xem có ghế nào đang bị khoá bởi người khác không
        for (Integer seatId : req.getSeatIds()) {
            String key = req.getShowtimeId() + "_" + seatId;
            Long expiry = seatLocks.get(key);
            if (expiry != null && expiry > now)
                return false; // Thất bại, có ghế đã bị tranh
        }
        // Khoá thành công, lưu vào bộ nhớ 5 phút
        for (Integer seatId : req.getSeatIds()) {
            seatLocks.put(req.getShowtimeId() + "_" + seatId, now + LOCK_TIME_MS);
        }
        return true;
    }

    @Override
    public void releaseSeats(BookingDTOs.SelectSeatReqDTO req) {
        for (Integer seatId : req.getSeatIds()) {
            seatLocks.remove(req.getShowtimeId() + "_" + seatId);
        }
    }

    @Override
    public Integer confirmBooking(BookingDTOs.ConfirmBookingReqDTO req, String email) {
        // Sẽ được gọi ở Controller sau (Logic Insert DATVE và VE)
        return null;
    }
}