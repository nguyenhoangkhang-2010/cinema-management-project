package com.example.project_web_cinema.entity.moviescreening;

import com.example.project_web_cinema.entity.movie.Movie;
import com.example.project_web_cinema.entity.movietheater.Room;
import com.example.project_web_cinema.entity.tickets.Tickets;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "SUATCHIEU",
    indexes = {
        @Index(name = "idx_ngaychieu", columnList = "NgayChieu")
    },
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "MaPhong", "NgayChieu", "GioBatDau"
        })
    }
)
public class MovieScreening {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maSuatChieu;
    @ManyToOne
    @JoinColumn(name = "MaPhim", nullable = false)
    private Movie movie;
    @ManyToOne
    @JoinColumn(name = "MaPhong", nullable = false)
    private Room room;
    @Column(name = "NgayChieu", nullable = false)
    private LocalDate ngayChieu;
    @Column(name = "GioBatDau", nullable = false)
    private LocalTime gioBatDau;
    @Column(name = "GioKetThuc", nullable = false)
    private LocalTime gioKetThuc;
    @Column(name = "GiaVe", nullable = false)
    private BigDecimal giaVe;

    @OneToMany(mappedBy = "movieScreening")
    private List<Tickets> dsVe;
}
