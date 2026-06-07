package com.example.project_web_cinema.service;

import com.example.project_web_cinema.dto.user.SeatDTO;
import com.example.project_web_cinema.dto.user.SeatSelectionResponseDTO;
import com.example.project_web_cinema.repository.ShowtimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatSelectionServiceImpl implements SeatSelectionService {

    private final ShowtimeRepository showtimeRepository;

    @Override
    public SeatSelectionResponseDTO getSeatSelectionData(Integer showtimeId) {
        List<Object[]> showtimeInfo = showtimeRepository.getShowtimeInfoForBooking(showtimeId);
        if (showtimeInfo.isEmpty()) {
            throw new RuntimeException("Không tìm thấy suất chiếu");
        }

        Object[] st = showtimeInfo.get(0);

        List<Object[]> seats = showtimeRepository.getSeatsForBooking(showtimeId);
        List<SeatDTO> seatList = new ArrayList<>();
        for (Object[] s : seats) {
            seatList.add(SeatDTO.builder()
                    .maGhe((Integer) s[0])
                    .soGhe((String) s[1])
                    .loaiGhe((String) s[2])
                    .phuThu(((Number) s[3]).doubleValue())
                    .daDat(((Number) s[4]).intValue() > 0)
                    .build());
        }

        return SeatSelectionResponseDTO.builder()
                .showtimeId(showtimeId)
                .movieName((String) st[0])
                .poster((String) st[1])
                .showDate(st[2].toString())
                .showTime(st[3].toString())
                .roomName((String) st[4])
                .cinemaName((String) st[5])
                .basePrice(((Number) st[6]).doubleValue())
                .seats(seatList)
                .build();
    }
}