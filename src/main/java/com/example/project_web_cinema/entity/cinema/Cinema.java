package com.example.project_web_cinema.entity.cinema;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "RAPPHIM")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cinema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer MaRap;

    @Column(name = "TenRap", nullable = false, length = 100)
    private String TenRap;

    @Column(name = "DiaChi", nullable = false, length = 255)
    private String DiaChi;

    @Column(name = "SoDienThoai", nullable = false, length = 15)
    private String SoDienThoai;
}
