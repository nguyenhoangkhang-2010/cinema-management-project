package com.example.project_web_cinema.entity.cinema;

import com.example.project_web_cinema.entity.movietheater.Room;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @Column(name = "SoDienThoai", length = 15)
    private String soDienThoai;

    @OneToMany(mappedBy = "cinema", cascade = CascadeType.ALL)
    private List<Room> dsPhong;
}
