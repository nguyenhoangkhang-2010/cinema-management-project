package com.example.project_web_cinema.controller.api;

import com.example.project_web_cinema.dto.user.SeatDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/checkout")
@RequiredArgsConstructor
public class BookingFlowApiController {

    @PersistenceContext
    private EntityManager entityManager;

    // 1. Lấy danh sách Rạp theo Phim
    @GetMapping("/cinemas/{movieId}")
    public ResponseEntity<?> getCinemasByMovie(@PathVariable Integer movieId) {
        String sql = "SELECT DISTINCT c.MaRap, c.TenRap FROM SUATCHIEU s JOIN PHONGCHIEU r ON s.MaPhong = r.MaPhong JOIN RAPPHIM c ON r.MaRap = c.MaRap WHERE s.MaPhim = ?";
        List<Object[]> rows = entityManager.createNativeQuery(sql).setParameter(1, movieId).getResultList();
        return ResponseEntity.ok(rows.stream().map(r -> Map.of("id", r[0], "name", r[1])).collect(Collectors.toList()));
    }

    // 2. Lấy danh sách Phòng theo Rạp
    @GetMapping("/rooms/{cinemaId}")
    public ResponseEntity<?> getRoomsByCinema(@PathVariable Integer cinemaId) {
        String sql = "SELECT MaPhong, TenPhong FROM PHONGCHIEU WHERE MaRap = ?";
        List<Object[]> rows = entityManager.createNativeQuery(sql).setParameter(1, cinemaId).getResultList();
        return ResponseEntity.ok(rows.stream().map(r -> Map.of("id", r[0], "name", r[1])).collect(Collectors.toList()));
    }

    // 3. Lấy sơ đồ ghế realtime (BOOKED vs AVAILABLE) theo Phòng và Suất chiếu
    @GetMapping("/seats/{roomId}/{showtimeId}")
    public ResponseEntity<List<SeatDTO>> getSeatsForMap(
            @PathVariable Integer roomId,
            @PathVariable Integer showtimeId) {

        String sql = """
                    SELECT g.MaGhe, g.SoGhe, lg.TenLoaiGhe, lg.GiaPhuThu,
                    (SELECT COUNT(*)
                    FROM VE v
                    JOIN DATVE dv ON v.MaDatVe = dv.MaDatVe
                    WHERE v.MaGhe = g.MaGhe
                    AND v.MaSuatChieu = ?
                    AND dv.TrangThai != 'DaHuy') as DaDat
                    FROM GHE g
                    JOIN LOAIGHE lg ON g.MaLoaiGhe = lg.MaLoaiGhe
                    WHERE g.MaPhong = ?
                """;

        List<Object[]> rows = entityManager.createNativeQuery(sql)
                .setParameter(1, showtimeId)
                .setParameter(2, roomId)
                .getResultList();

        // CASE 1: Có ghế trong DB
        if (!rows.isEmpty()) {
            return ResponseEntity.ok(
                    rows.stream().map(r -> SeatDTO.builder()
                            .maGhe((Integer) r[0])
                            .soGhe((String) r[1])
                            .loaiGhe((String) r[2])
                            .phuThu(((Number) r[3]).doubleValue())
                            .daDat(((Number) r[4]).intValue() > 0)
                            .build()).toList());
        }

        // CASE 2: KHÔNG có ghế → generate theo SoLuongGhe
        Integer totalSeats = ((Number) entityManager.createNativeQuery(
                "SELECT SoLuongGhe FROM PHONGCHIEU WHERE MaPhong = ?")
                .setParameter(1, roomId)
                .getSingleResult()).intValue();
        List<SeatDTO> generated = new ArrayList<>();

        for (int i = 1; i <= totalSeats; i++) {

            String seatCode = "A" + i;

            entityManager.createNativeQuery("""
                        INSERT INTO GHE (MaPhong, MaLoaiGhe, SoGhe)
                        VALUES (?, 1, ?)
                    """)
                    .setParameter(1, roomId)
                    .setParameter(2, seatCode)
                    .executeUpdate();

            generated.add(SeatDTO.builder()
                    .maGhe(i)
                    .soGhe(seatCode)
                    .loaiGhe("STANDARD")
                    .phuThu(0.0)
                    .daDat(false)
                    .build());
        }

        return ResponseEntity.ok(generated);
    }
}
