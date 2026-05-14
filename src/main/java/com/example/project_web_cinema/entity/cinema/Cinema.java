package com.example.project_web_cinema.entity.cinema;

import com.example.project_web_cinema.entity.movietheater.Room;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    @Column(name = "MaRap")
    private Integer maRap;

    @NotNull
    @Column(name = "TenRap", nullable = false, length = 100)
    private String tenRap;

    @Column(name = "DiaChi", length = 255)
    private String diaChi;

    @Pattern(regexp = "^[0-9]{10,11}$")
    @Column(name = "SoDienThoai", length = 15)
    private String soDienThoai;

    @OneToMany(mappedBy = "cinema", cascade = CascadeType.ALL)
    private List<Room> dsPhong;
}
