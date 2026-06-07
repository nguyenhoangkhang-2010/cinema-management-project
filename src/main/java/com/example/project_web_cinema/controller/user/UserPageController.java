package com.example.project_web_cinema.controller.user;

import com.example.project_web_cinema.entity.movie.Movie;
import com.example.project_web_cinema.repository.MovieRepository;
import com.example.project_web_cinema.service.UserStoreService;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserPageController {

    private final UserStoreService userStoreService;
    private final MovieRepository movieRepository;

    @Autowired
    public UserPageController(UserStoreService userStoreService, MovieRepository movieRepository) {
        this.userStoreService = userStoreService;
        this.movieRepository = movieRepository;
    }


    @Data
    @Builder
    public static class EventDTO {
        private String tieuDe;
        private String tomTat;
        private String anhBanner;
        private String loaiSuKien;
        private String tag;
    }

    @GetMapping("/store")
    public String store(@RequestParam(required = false) String search,
                        @RequestParam(defaultValue = "1") int page, Model model) {
        if (search != null && !search.trim().isEmpty()) {
            model.addAttribute("storePage", userStoreService.searchMovie(search, PageRequest.of(page - 1, 8)));
            model.addAttribute("search", search);
        } else {
            model.addAttribute("storePage", userStoreService.getNowShowingMovies(PageRequest.of(page - 1, 8)));
        }
        return "user/store";
    }

    @GetMapping("/events")
    public String events(Model model) {
        model.addAttribute("featuredPromos", userStoreService.getFeaturedPromotions());
        model.addAttribute("activePromos", userStoreService.getActivePromotions());
        model.addAttribute("sneakPeek", userStoreService.getUpcomingMovies());

        List<EventDTO> dsSuKien = new ArrayList<>();

        dsSuKien.add(EventDTO.builder()
                .tieuDe("Thứ Hai All Star - Đồng Giá Vé 50K")
                .tomTat("Cơ hội trải nghiệm những siêu phẩm điện ảnh đầu tuần với mức giá siêu hạt dẻ chỉ 50.000đ/vé áp dụng cho mọi suất chiếu 2D.")
                .anhBanner("https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?q=80&w=800")
                .loaiSuKien("featured")
                .tag("ĐỒNG GIÁ")
                .build());

        dsSuKien.add(EventDTO.builder()
                .tieuDe("Ngày Hội Học Sinh Sinh Viên - Đồng Giá 45K")
                .tomTat("Đặc quyền nhận mức giá ưu đãi dành riêng cho các bạn học sinh và sinh viên khi xuất trình thẻ tại quầy vé Cinema All Star.")
                .anhBanner("https://images.unsplash.com/photo-1523240795612-9a054b0db644?q=80&w=800")
                .loaiSuKien("active")
                .tag("HỌC ĐƯỜNG")
                .build());

        dsSuKien.add(EventDTO.builder()
                .tieuDe("Combo Đôi Trọn Vẹn - Tặng Voucher 20%")
                .tomTat("Thưởng thức bắp rang bơ khổng lồ cùng 2 ly nước lớn ngọt ngào, nhận ngay voucher giảm giá cho lần xem phim kế tiếp.")
                .anhBanner("https://images.unsplash.com/photo-1578496781985-452d4a934d50?q=80&w=800")
                .loaiSuKien("active")
                .tag("ẨM THỰC")
                .build());

        try {
            List<Movie> dsMovieTuDB = movieRepository.findAll();
            int count = 0;
            for (Movie movie : dsMovieTuDB) {
                if (count >= 3) break;

                dsSuKien.add(EventDTO.builder()
                        .tieuDe("Sự Kiện Ra Mắt Siêu Phẩm: " + movie.getTenPhim().toUpperCase())
                        .tomTat(movie.getMoTa() != null && !movie.getMoTa().isEmpty() ? movie.getMoTa() : "Đặt vé ngay hôm nay để nhận poster giới hạn và quà lưu niệm độc quyền từ rạp phim Cinema All Star.")
                        .anhBanner(movie.getPoster() != null && !movie.getPoster().isEmpty() ? movie.getPoster() : "https://i.pinimg.com/1200x/ea/62/2d/ea622daa97869d34ef4e119d41355de3.jpg")
                        .loaiSuKien("movie-event") // Khớp tab sự kiện phim
                        .tag("BOM TẤN")
                        .build());
                count++;
            }
        } catch (Exception e) {
            System.out.println("Lỗi đồng bộ dữ liệu phim: " + e.getMessage());
        }

        model.addAttribute("danhSachSuKien", dsSuKien);
        return "user/events";
    }

    @GetMapping("/movie/{id}")
    public String movieDetail(@PathVariable Integer id, Model model) {
        model.addAttribute("movie", userStoreService.getMovieDetail(id));
        return "user/movie-detail";
    }

    @GetMapping("/promotion/{id}")
    public String promotionDetail(@PathVariable Integer id, Model model) {
        model.addAttribute("promotion", userStoreService.getPromotionDetail(id));
        return "user/promotion-detail";
    }
}