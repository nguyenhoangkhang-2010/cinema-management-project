package com.example.project_web_cinema.dto.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private Integer maTheLoai;

    @NotBlank(message = "Tên thể loại không được để trống")
    private String tenLoai;
    private Integer soPhimDangSuDung;
}