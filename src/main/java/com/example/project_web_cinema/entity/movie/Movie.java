package com.example.project_web_cinema.entity.movie;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "RAPPHIM")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer MaPhim;
    @Column(name = "TenPhim", nullable = false, length = 255)
    private String TenPhim;
}
