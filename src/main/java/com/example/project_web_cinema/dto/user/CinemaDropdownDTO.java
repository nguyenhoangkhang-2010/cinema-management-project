package com.example.project_web_cinema.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CinemaDropdownDTO {
    private Integer cinemaId;
    private String cinemaName;
}