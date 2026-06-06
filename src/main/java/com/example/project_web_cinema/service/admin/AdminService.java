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
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminService {
    private final MovieService movieService;
    private final PromotionService promotionService;

    private final MovieRepository movieRepository;
    private final TicketRepository ticketRepository;
    private final AccountRepository accountRepository;

    @PersistenceContext
    private EntityManager em;

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

        Double totalRev = 0.0;
        try {
            BigDecimal totalRevBd = em
                    .createQuery("SELECT SUM(p.soTien) FROM Pay p WHERE p.trangThai = 'ThanhCong'", BigDecimal.class)
                    .getSingleResult();
            totalRev = totalRevBd != null ? totalRevBd.doubleValue() : 0.0;
        } catch (Exception e) {
            totalRev = 0.0;
        }

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
                LocalDateTime weekAgoRev = LocalDate.now().minusDays(6).atStartOfDay();
                List<com.example.project_web_cinema.entity.pay.Pay> pays = em.createQuery(
                        "SELECT p FROM Pay p WHERE p.trangThai = 'ThanhCong' AND p.ngayThanhToan >= :weekAgo",
                        com.example.project_web_cinema.entity.pay.Pay.class)
                        .setParameter("weekAgo", weekAgoRev).getResultList();
                Map<LocalDate, Double> revMap = pays.stream().collect(Collectors.groupingBy(
                        p -> p.getNgayThanhToan().toLocalDate(),
                        Collectors.summingDouble(p -> p.getSoTien().doubleValue())));
                data.put("values", build7DaysValues(revMap));
                data.put("labels", build7DaysLabels());
                break;
            case "doanhthu-thang":
                List<Object[]> monthData = em.createQuery(
                        "SELECT FUNCTION('MONTH', p.ngayThanhToan), SUM(p.soTien) " +
                                "FROM Pay p WHERE p.trangThai = 'ThanhCong' AND FUNCTION('YEAR', p.ngayThanhToan) = FUNCTION('YEAR', CURRENT_DATE) "
                                +
                                "GROUP BY FUNCTION('MONTH', p.ngayThanhToan)",
                        Object[].class).getResultList();
                List<Double> monthValues = new ArrayList<>(Collections.nCopies(12, 0.0));
                for (Object[] row : monthData) {
                    int month = ((Number) row[0]).intValue();
                    double sum = ((BigDecimal) row[1]).doubleValue();
                    monthValues.set(month - 1, sum);
                }
                data.put("values", monthValues);
                data.put("labels", Arrays.asList("Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6",
                        "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"));
                break;

            case "ve":
                LocalDateTime weekAgoVe = LocalDate.now().minusDays(6).atStartOfDay();
                List<com.example.project_web_cinema.entity.tickets.Tickets> tickets = em.createQuery(
                        "SELECT t FROM Tickets t JOIN FETCH t.bookTickets b WHERE b.ngayDat >= :weekAgo",
                        com.example.project_web_cinema.entity.tickets.Tickets.class)
                        .setParameter("weekAgo", weekAgoVe).getResultList();
                Map<LocalDate, Long> veMap = tickets.stream().collect(Collectors.groupingBy(
                        t -> t.getBookTickets().getNgayDat().toLocalDate(),
                        Collectors.counting()));
                data.put("values", build7DaysValuesLong(veMap));
                data.put("labels", build7DaysLabels());
                break;
            case "doanhthu-phuongthuc":
                List<Object[]> methodData = em.createQuery(
                        "SELECT p.phuongThuc, SUM(p.soTien) FROM Pay p WHERE p.trangThai = 'ThanhCong' GROUP BY p.phuongThuc",
                        Object[].class).getResultList();
                List<String> methodLabels = new ArrayList<>();
                List<Double> methodValues = new ArrayList<>();
                for (Object[] row : methodData) {
                    methodLabels.add(row[0] != null ? row[0].toString() : "Khác");
                    methodValues.add(((BigDecimal) row[1]).doubleValue());
                }
                data.put("values", methodValues);
                data.put("labels", methodLabels);
                break;

            case "khachhang":
                LocalDateTime weekAgoKh = LocalDate.now().minusDays(6).atStartOfDay();
                List<com.example.project_web_cinema.entity.account.Account> accounts = em.createQuery(
                        "SELECT a FROM Account a WHERE a.ngayTao >= :weekAgo",
                        com.example.project_web_cinema.entity.account.Account.class)
                        .setParameter("weekAgo", weekAgoKh).getResultList();
                Map<LocalDate, Long> khMap = accounts.stream().collect(Collectors.groupingBy(
                        a -> a.getNgayTao().toLocalDate(),
                        Collectors.counting()));
                data.put("values", build7DaysValuesLong(khMap));
                data.put("labels", build7DaysLabels());
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

    private List<String> build7DaysLabels() {
        List<String> labels = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            labels.add(LocalDate.now().minusDays(i).format(DateTimeFormatter.ofPattern("dd/MM")));
        }
        return labels;
    }

    private List<Double> build7DaysValues(Map<LocalDate, Double> map) {
        List<Double> values = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            values.add(map.getOrDefault(LocalDate.now().minusDays(i), 0.0));
        }
        return values;
    }

    private List<Long> build7DaysValuesLong(Map<LocalDate, Long> map) {
        List<Long> values = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            values.add(map.getOrDefault(LocalDate.now().minusDays(i), 0L));
        }
        return values;
    }
}
