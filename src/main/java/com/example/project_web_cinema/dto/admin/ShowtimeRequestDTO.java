package com.example.project_web_cinema.dto.admin;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ShowtimeRequestDTO {
    @NotNull(message = "Vui lòng chọn phim")
    private Integer maPhim;

    @NotNull(message = "Vui lòng chọn phòng chiếu")
    private Integer maPhong;

    @NotNull(message = "Vui lòng nhập ngày chiếu")
    private LocalDate ngayChieu;

    @NotNull(message = "Vui lòng nhập giờ bắt đầu")
    private LocalTime gioBatDau;

    @NotNull(message = "Vui lòng nhập giờ kết thúc")
    private LocalTime gioKetThuc;

    @NotNull(message = "Giá vé không được để trống")
    @Positive(message = "Giá vé phải lớn hơn 0")
    private BigDecimal giaVe;
}