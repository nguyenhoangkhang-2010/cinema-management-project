package com.example.project_web_cinema.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Data
@AllArgsConstructor
public class MovieSliderDTO {
    private Integer maPhim;
    private String tenPhim;
    private String poster;
    private LocalDate ngayKhoiChieu;
    private String trailer;

    public long getDaysUntilRelease() {
        if (ngayKhoiChieu == null)
            return 0;
        long days = ChronoUnit.DAYS.between(LocalDate.now(), ngayKhoiChieu);
        return days > 0 ? days : 0;
    }
}