package com.example.project_web_cinema.entity.rate;

import com.example.project_web_cinema.entity.account.Account;
import com.example.project_web_cinema.entity.movie.Movie;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "DANHGIA")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaDanhGia")
    private Integer maDanhGia;

    @Column(name = "SoSao")
    private Integer soSao;

    @Column(name = "BinhLuan")
    private String binhLuan;

    @Column(name = "NgayDanhGia", insertable = false, updatable = false)
    private java.time.LocalDateTime ngayDanhGia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaTaiKhoan")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaPhim")
    private Movie movie;
}