package com.example.project_web_cinema.entity.category;

import com.example.project_web_cinema.entity.movie.Movie;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "THELOAI")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maTheLoai;
    @Column(name = "TenLoai", nullable = false, unique = true)
    private String tenLoai;

    @ManyToMany(mappedBy = "dsTheLoai")
    private List<Movie> dsPhim;
}
