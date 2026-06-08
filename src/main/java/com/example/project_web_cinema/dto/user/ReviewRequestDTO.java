package com.example.project_web_cinema.dto.user;

import lombok.Data;

@Data
public class ReviewRequestDTO {
    private Integer movieId;
    private Integer rating;
    private String comment;
}