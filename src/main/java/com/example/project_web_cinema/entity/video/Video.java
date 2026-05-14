package com.example.project_web_cinema.entity.video;

import com.example.project_web_cinema.entity.movie.Movie;
import jakarta.persistence.*;
import lombok.*;

import javax.naming.Name;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "VIDEO")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaVideo")
    private Integer maVideo;
    @OneToOne
    @JoinColumn(name = "MaPhim", nullable = false, unique = true)
    private Movie movie;
    @Column(name = "DuongDanVideo", nullable = false, length = 500)
    private String duongDanVideo;
    @Column(name = "ChatLuong", length = 20)
    private String chatLuong;
}
