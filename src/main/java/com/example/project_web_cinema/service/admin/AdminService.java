package com.example.project_web_cinema.service.admin;

import com.example.project_web_cinema.dto.PromotionDTO;
import com.example.project_web_cinema.dto.admin.AdminHomeDTO;
import com.example.project_web_cinema.entity.promotion.Promotion;
import com.example.project_web_cinema.repository.BookingRepository;
import com.example.project_web_cinema.repository.MovieRepository;
import com.example.project_web_cinema.repository.UserRepository;
import com.example.project_web_cinema.service.MovieService;
import com.example.project_web_cinema.service.PromotionService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdminService {
    private final MovieService movieService;
    private final PromotionService promotionService;

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    public AdminService(MovieService movieService,
            PromotionService promotionService,
            BookingRepository bookingRepository,
            UserRepository userRepository,
            MovieRepository movieRepository) {
        this.movieService = movieService;
        this.promotionService = promotionService;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    public AdminHomeDTO getAdminHome() {
        long countPromotions = promotionService.getAllPromotions() != null ? promotionService.getAllPromotions().size()
                : 0;
        return AdminHomeDTO.builder()
                .phimDangChieu(movieService.getMoviesDangChieu())
                .khuyenMai(promotionService.getAllPromotions())
                .totalMovies(movieService.countAllMovies())
                .totalPromotions(countPromotions)
                .build();
    }

    public long totalMovies() {
        return movieService.countAllMovies();
    }

    public List<PromotionDTO> getAllPromotions() {
        return promotionService.getAllPromotions();
    }

    public Map<String, Object> getChartDataByType(String type) {
        Map<String, Object> data = new HashMap<>();

        switch (type.toLowerCase()) {
            case "doanhthu":
                data.put("values", bookingRepository.getRevenueLast7Days());
                data.put("labels",
                        Arrays.asList("Ngày 7", "Ngày 6", "Ngày 5", "Ngày 4", "Ngày 3", "Ngày 2", "Hôm nay"));
                break;

            case "ve":
                data.put("values", bookingRepository.getTicketCountLast7Days());
                data.put("labels",
                        Arrays.asList("Ngày 7", "Ngày 6", "Ngày 5", "Ngày 4", "Ngày 3", "Ngày 2", "Hôm nay"));
                break;

            case "khachhang":
                data.put("values", userRepository.getUserCountLast7Days());
                data.put("labels",
                        Arrays.asList("Ngày 7", "Ngày 6", "Ngày 5", "Ngày 4", "Ngày 3", "Ngày 2", "Hôm nay"));
                break;

            case "phim":
                data.put("values", movieRepository.getMovieCountByStatus());
                data.put("labels", Arrays.asList("Sắp chiếu", "Đang chiếu", "Ngừng chiếu"));
                break;

            default:
                data.put("values", Collections.emptyList());
                data.put("labels", Collections.emptyList());
        }
        return data;
    }
}
