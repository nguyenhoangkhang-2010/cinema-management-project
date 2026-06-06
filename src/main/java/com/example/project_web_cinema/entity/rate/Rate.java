package com.example.project_web_cinema.entity.rate;


import com.example.project_web_cinema.entity.account.Account;
import com.example.project_web_cinema.entity.movie.Movie;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "DANHGIA", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"MaTaiKhoan", "MaPhim"})
})
public class Rate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaDanhGia")
    private Integer maDanhGia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaTaiKhoan", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaPhim", nullable = false)
    private Movie movie;

    @Column(name = "SoSao", nullable = false)
    private Integer soSao;

    @Column(name = "BinhLuan", columnDefinition = "TEXT")
    private String binhLuan;

    @CreationTimestamp
    @Column(name = "NgayDanhGia", updatable = false)
    private LocalDateTime ngayDanhGia;
}
