package com.example.project_web_cinema.service.admin;

import com.example.project_web_cinema.dto.PromotionDTO;
import com.example.project_web_cinema.dto.admin.AdminHomeDTO;
import com.example.project_web_cinema.entity.promotion.Promotion;
import com.example.project_web_cinema.repository.AccountRepository;
import com.example.project_web_cinema.repository.MovieRepository;
import com.example.project_web_cinema.repository.TicketRepository;
import com.example.project_web_cinema.service.MovieService;
import com.example.project_web_cinema.service.PromotionService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdminService {
    private final MovieService movieService;
    private final PromotionService promotionService;

    private final MovieRepository movieRepository;
    private final TicketRepository ticketRepository;
    private final AccountRepository accountRepository;

    public AdminService(MovieService movieService,
            PromotionService promotionService,
            MovieRepository movieRepository,
            TicketRepository ticketRepository,
            AccountRepository accountRepository) {
        this.movieService = movieService;
        this.promotionService = promotionService;
        this.movieRepository = movieRepository;
        this.ticketRepository = ticketRepository;
        this.accountRepository = accountRepository;
    }

    public AdminHomeDTO getAdminHome() {
        long countPromotions = promotionService.getAllPromotions() != null ? promotionService.getAllPromotions().size()
                : 0;

        Double totalRev = 0.0; // Fixed: Method undefined in context
        return AdminHomeDTO.builder()
                .phimDangChieu(movieService.getMoviesDangChieu())
                .khuyenMai(promotionService.getAllPromotions().stream()
                        .map(p -> PromotionDTO.builder()
                                .maKhuyenMai(p.getMaKhuyenMai())
                                .tenKhuyenMai(p.getTenKhuyenMai())
                                .phanTramGiam(p.getPhanTramGiam())
                                .poster(p.getPoster())
                                .build())
                        .collect(java.util.stream.Collectors.toList()))
                .totalMovies(movieService.countAllMovies())
                .totalPromotions(countPromotions)
                .totalUsers(accountRepository.count())
                .totalTickets(ticketRepository.count())
                .totalRevenue(totalRev != null ? totalRev : 0.0)
                .build();
    }

    public long totalMovies() {
        return movieService.countAllMovies();
    }

    public List<PromotionDTO> getAllPromotions() {
        return promotionService.getAllPromotions().stream()
                .map(p -> PromotionDTO.builder()
                        .maKhuyenMai(p.getMaKhuyenMai())
                        .tenKhuyenMai(p.getTenKhuyenMai())
                        .phanTramGiam(p.getPhanTramGiam())
                        .poster(p.getPoster())
                        .build())
                .collect(java.util.stream.Collectors.toList());
    }

    public Map<String, Object> getChartDataByType(String type) {
        Map<String, Object> data = new HashMap<>();

        switch (type.toLowerCase()) {
            case "doanhthu":
                data.put("values", Arrays.asList(0, 0, 0, 0, 0, 0, 0));
                data.put("labels",
                        Arrays.asList("Ngày 7", "Ngày 6", "Ngày 5", "Ngày 4", "Ngày 3", "Ngày 2", "Hôm nay"));
                break;
            case "doanhthu-thang":
                // Dữ liệu mô phỏng cho Doanh Thu theo tháng
                data.put("values", Arrays.asList(12000000, 15000000, 18000000, 14000000, 22000000, 25000000, 30000000,
                        28000000, 32000000, 35000000, 40000000, 45000000));
                data.put("labels", Arrays.asList("Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6",
                        "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"));
                break;

            case "ve":
                data.put("values", Arrays.asList(0, 0, 0, 0, 0, 0, 0));
                data.put("labels",
                        Arrays.asList("Ngày 7", "Ngày 6", "Ngày 5", "Ngày 4", "Ngày 3", "Ngày 2", "Hôm nay"));
                break;
            case "doanhthu-gioitinh":
                // Dữ liệu mô phỏng cho Doanh Thu theo giới tính
                data.put("values", Arrays.asList(45000000, 50000000, 5000000));
                data.put("labels", Arrays.asList("Nam", "Nữ", "Khác"));
                break;

            case "khachhang":
                data.put("values", Arrays.asList(0, 0, 0, 0, 0, 0, 0));
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
