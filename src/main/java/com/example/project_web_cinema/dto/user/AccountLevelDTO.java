package com.example.project_web_cinema.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountLevelDTO {
    private Integer maTaiKhoan;
    private String hoTen;
    private Integer tongSoVeDaMua;
    private Integer capDo;
    private Integer soVeConThieuDeLenCap;

    private Integer phanTramTienTrinh; // Dùng để vẽ Progress Bar linh động trên Frontend
}