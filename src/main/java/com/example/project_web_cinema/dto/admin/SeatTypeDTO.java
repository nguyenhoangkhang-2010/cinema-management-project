package com.example.project_web_cinema.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatTypeDTO {
    private Integer maLoaiGhe;

    @NotBlank(message = "Tên loại ghế không được để trống")
    private String tenLoaiGhe;

    @NotNull(message = "Giá phụ thu không được để trống")
    private Double giaPhuThu;
}
