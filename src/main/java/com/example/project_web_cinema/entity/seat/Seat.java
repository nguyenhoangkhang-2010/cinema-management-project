package com.example.project_web_cinema.entity.seat;

import com.example.project_web_cinema.entity.movietheater.Room;
import com.example.project_web_cinema.entity.tickets.Tickets;
import com.example.project_web_cinema.entity.typeofseat.TypeOfSeat;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "GHE",
    uniqueConstraints = {
            @UniqueConstraint(columnNames = {"SoGhe", "MaPhong"})
    }
)
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaGhe")
    private Integer maGhe;
    @ManyToOne
    @JoinColumn(name = "MaLoaiGhe", nullable = false)
    private TypeOfSeat maLoaiGhe;
    @Column(name = "SoGhe", nullable = false, length = 10)
    private String soGhe;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaPhong", nullable = false)
    private Room room;

    @OneToMany(mappedBy = "seat")
    private List<Tickets> dsVe;
}
