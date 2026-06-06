package com.example.project_web_cinema.service.admin;

import com.example.project_web_cinema.dto.admin.BookingResponseDTO;
import com.example.project_web_cinema.entity.booktickets.BookTickets;
import com.example.project_web_cinema.entity.booktickets.TrangThaiDatVe;
import com.example.project_web_cinema.entity.pay.TrangThaiThanhToan;
import com.example.project_web_cinema.repository.BookingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.example.project_web_cinema.entity.pay.Pay;
import com.example.project_web_cinema.entity.tickets.Tickets;
import com.example.project_web_cinema.entity.moviescreening.MovieScreening;

import java.util.stream.Collectors;

@Service
public class AdminBookingService {

    private final BookingRepository bookingRepository;

    public AdminBookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Page<BookingResponseDTO> searchBookings(String search, String statusOrderStr, String statusPaymentStr,
            String sort, int page, int size) {
        TrangThaiDatVe statusOrder = null;
        if (statusOrderStr != null && !statusOrderStr.isEmpty()) {
            try {
                statusOrder = TrangThaiDatVe.valueOf(statusOrderStr);
            } catch (Exception ignored) {
            }
        }

        TrangThaiThanhToan statusPayment = null;
        if (statusPaymentStr != null && !statusPaymentStr.isEmpty()) {
            try {
                statusPayment = TrangThaiThanhToan.valueOf(statusPaymentStr);
            } catch (Exception ignored) {
            }
        }

        // Xử lý các điều kiện Sắp xếp
        Sort sortObj = Sort.by(Sort.Direction.DESC, "ngayDat"); // Mặc định Mới nhất
        if ("oldest".equals(sort)) {
            sortObj = Sort.by(Sort.Direction.ASC, "ngayDat");
        } else if ("price_asc".equals(sort)) {
            sortObj = Sort.by(Sort.Direction.ASC, "tongTien");
        } else if ("price_desc".equals(sort)) {
            sortObj = Sort.by(Sort.Direction.DESC, "tongTien");
        }

        Pageable pageable = PageRequest.of(page - 1, size, sortObj);

        Page<BookTickets> entityPage = bookingRepository.findBookingsWithFilters(
                (search != null && !search.isEmpty()) ? search : null,
                statusOrder,
                statusPayment,
                pageable);

        return entityPage.map(this::convertToDTO);
    }

    public BookingResponseDTO getBookingDetail(Integer id) {
        BookTickets entity = bookingRepository.findById(id).orElse(null);
        if (entity == null)
            return null;
        return convertToDTO(entity);
    }

    private BookingResponseDTO convertToDTO(BookTickets entity) {
        String danhSachGhe = "N/A";
        if (entity.getDsVe() != null && !entity.getDsVe().isEmpty()) {
            danhSachGhe = entity.getDsVe().stream()
                    .map(t -> t.getSeat() != null ? String.valueOf(t.getSeat().getSoGhe()) : "")
                    .collect(Collectors.joining(", "));
        }

        Pay pay = (entity.getDsThanhToan() != null && !entity.getDsThanhToan().isEmpty())
                ? entity.getDsThanhToan().get(0)
                : null;

        // Lấy thông tin suất chiếu từ vé đầu tiên thuộc đơn đặt vé này
        Tickets firstTicket = (entity.getDsVe() != null && !entity.getDsVe().isEmpty()) ? entity.getDsVe().get(0)
                : null;
        MovieScreening suatChieu = (firstTicket != null) ? firstTicket.getMovieScreening() : null;

        return BookingResponseDTO.builder()
                .maDatVe(entity.getMaDatVe())
                .hoTenKhachHang(entity.getAccount() != null ? entity.getAccount().getHoTen() : "Khách ẩn danh")
                .email(entity.getAccount() != null ? entity.getAccount().getEmail() : "")
                .soDienThoai(entity.getAccount() != null ? entity.getAccount().getSoDienThoai() : "")
                .tenPhim(suatChieu != null && suatChieu.getMovie() != null
                        ? suatChieu.getMovie().getTenPhim()
                        : "N/A")
                .poster(suatChieu != null && suatChieu.getMovie() != null
                        ? suatChieu.getMovie().getPoster()
                        : "")
                .ngayChieu(suatChieu != null && suatChieu.getNgayChieu() != null
                        ? suatChieu.getNgayChieu().toString()
                        : "N/A")
                .gioBatDau(suatChieu != null && suatChieu.getGioBatDau() != null
                        ? suatChieu.getGioBatDau().toString()
                        : "N/A")
                .tenPhong(suatChieu != null && suatChieu.getRoom() != null
                        ? suatChieu.getRoom().getTenPhong()
                        : "N/A")
                .danhSachGhe(danhSachGhe)
                .ngayDat(entity.getNgayDat())
                .tongTien(entity.getTongTien())
                .tenKhuyenMai(entity.getPromotion() != null ? entity.getPromotion().getTenKhuyenMai() : "Không áp dụng")
                .phuongThucThanhToan(pay != null && pay.getPhuongThuc() != null
                        ? pay.getPhuongThuc().name()
                        : "N/A")
                .trangThaiDatVe(entity.getTrangThaiDatVe() != null ? entity.getTrangThaiDatVe().name() : "")
                .trangThaiThanhToan(pay != null && pay.getTrangThai() != null
                        ? pay.getTrangThai().name()
                        : "")
                .build();
    }
}