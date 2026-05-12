package com.example.project_web_cinema.entity.typeofseat;

import com.example.project_web_cinema.entity.seat.Seat;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "LOAIGHE")
public class TypeOfSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maLoaiGhe;
    @Column(name = "TenLoaiGhe", nullable = false, unique = true, length = 30)
    private String tenLoaiGhe;
    @Column(name = "GiaPhuThu", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal giaPhuThu = BigDecimal.ZERO;

    @OneToMany(mappedBy = "maLoaiGhe", cascade = CascadeType.ALL)
    private List<Seat> dsGhe;
}
