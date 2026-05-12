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
    private Integer maRap;

    @Column(name = "TenRap", nullable = false, length = 100)
    private String tenRap;

    @Column(name = "DiaChi", nullable = false, length = 255)
    private String diaChi;

    @Column(name = "SoDienThoai", nullable = false, length = 15)
    private String soDienThoai;
}
