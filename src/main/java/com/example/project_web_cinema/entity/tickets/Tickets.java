package com.example.project_web_cinema.entity.tickets;

import com.example.project_web_cinema.entity.booktickets.BookTickets;
import com.example.project_web_cinema.entity.moviescreening.MovieScreening;
import com.example.project_web_cinema.entity.seat.Seat;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "VE",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"MaSuatChieu", "MaGhe"})
    },
    indexes = {
        @Index(name = "idx_ve_suatchieu", columnList = "MaSuatChieu"),
        @Index(name = "idx_ve_ghe", columnList = "MaGhe")
    }
)
public class Tickets {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaVe")
    private Integer maVe;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaDatVe", nullable = false)
    private BookTickets bookTickets;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaSuatChieu", nullable = false)
    private MovieScreening movieScreening;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaGhe", nullable = false)
    private Seat seat;
}
