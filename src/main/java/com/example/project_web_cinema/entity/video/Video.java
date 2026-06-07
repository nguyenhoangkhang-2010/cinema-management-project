package com.example.project_web_cinema.entity.video;

import com.example.project_web_cinema.entity.movie.Movie;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "VIDEO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaVideo")
    private Integer maVideo;

    @Column(name = "DuongDanVideo")
    private String duongDan;

    @Column(name = "ChatLuong")
    private String chatLuong;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaPhim")
    private Movie movie;
}