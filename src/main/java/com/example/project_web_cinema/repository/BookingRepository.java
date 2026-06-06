package com.example.project_web_cinema.repository;

import com.example.project_web_cinema.entity.booktickets.BookTickets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<BookTickets, Integer> {
    @Query(value = "SELECT SUM(tongTien) FROM datve WHERE trangThai = 'DaThanhToan' GROUP BY DATE(ngayDat) ORDER BY ngayDat DESC LIMIT 7", nativeQuery = true)
    List<Double> getRevenueLast7Days();

    @Query(value = "SELECT COUNT(*) FROM ve v JOIN datve d ON v.maDatVe = d.maDatVe GROUP BY DATE(d.ngayDat) ORDER BY d.ngayDat DESC LIMIT 7", nativeQuery = true)
    List<Long> getTicketCountLast7Days();
}
