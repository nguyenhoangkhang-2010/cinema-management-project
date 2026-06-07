package com.example.project_web_cinema.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowtimeDateResponseDTO {
    private String date; // Định dạng chuẩn yyyy-MM-dd để gọi API tiếp theo
    private String displayDate; // Định dạng hiển thị UI (VD: 07/06)
}