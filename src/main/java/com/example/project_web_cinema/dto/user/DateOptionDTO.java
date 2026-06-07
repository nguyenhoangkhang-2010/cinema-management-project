package com.example.project_web_cinema.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DateOptionDTO {
    private String dateValue; // yyyy-MM-dd để gọi API
    private String displayDate; // dd/MM/yyyy để hiển thị UI
}