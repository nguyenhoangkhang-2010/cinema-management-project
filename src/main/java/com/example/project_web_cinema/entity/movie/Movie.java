package com.example.project_web_cinema.entity.movie;

import com.example.project_web_cinema.entity.category.Category;
import com.example.project_web_cinema.entity.moviescreening.MovieScreening;
import com.example.project_web_cinema.entity.video.Video;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "PHIM",
    indexes = {
        @Index(name = "idx_tenphim", columnList = "TenPhim")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaPhim")
    private Integer maPhim;
    @Column(name = "TenPhim", nullable = false, length = 255)
    private String tenPhim;
    @Column(name = "Poster", length = 255)
    private String poster;
    @Column(name = "Trailer", length = 255)
    private String trailer;
    @Column(name = "DaoDien", length = 100)
    private String daoDien;
    @Min(1)
    @Column(name = "ThoiLuong", nullable = false)
    private Integer thoiLuong;
    @Column(name = "NgayKhoiChieu", nullable = false)
    private LocalDate ngayKhoiChieu;
    @Column(name = "NgayKetThucChieu", nullable = false)
    private LocalDate ngayKetThucChieu;
    @Column(name = "QuocGia", length = 50)
    private String quocGia;
    @Lob
    @Column(name = "MoTa")
    private String moTa;
    @Min(0)
    @Column(name = "DoTuoi")
    @Builder.Default
    private Integer doTuoi=0;
    @Min(1)
    @Max(3)
    @Column(name = "CapDoYeuCau")
    @Builder.Default
    private Integer capDoYeuCau=1;
    @Enumerated(EnumType.STRING)
    @Column(name = "TrangThai")
    @Builder.Default
    private TrangThaiPhim trangThai = TrangThaiPhim.SapChieu;

    @OneToOne(mappedBy = "movie", cascade = CascadeType.ALL)
    private Video video;

    @ManyToMany
    @JoinTable(name = "PHIM_THELOAI",
            joinColumns = @JoinColumn(name = "MaPhim"),
            inverseJoinColumns = @JoinColumn(name = "MaTheLoai"))
    private List<Category> dsTheLoai;

    @OneToMany(mappedBy = "movie")
    private List<MovieScreening> dsSuatChieu;
}
