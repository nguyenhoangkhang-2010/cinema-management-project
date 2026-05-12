package com.example.project_web_cinema.entity.movietheater;

import com.example.project_web_cinema.entity.cinema.Cinema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "PHONGCHIEU")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maPhong;
    @Column(name = "TenPhong", nullable = false, length = 50)
    private String tenPhong;
    @Column(name = "SoLuongGhe", nullable = false)
    private Integer soLuongGhe;
    @ManyToOne
    @JoinColumn(name = "MaRap", nullable = false)
    private Cinema cinema;
}
