package com.example.project_web_cinema.controller.admin;

import com.example.project_web_cinema.dto.admin.BookingResponseDTO;
import com.example.project_web_cinema.service.admin.AdminBookingService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/bookings")
@PreAuthorize("hasRole('Admin')")
public class AdminBookingController {

    private final AdminBookingService adminBookingService;

    public AdminBookingController(AdminBookingService adminBookingService) {
        this.adminBookingService = adminBookingService;
    }

    @GetMapping
    public String getBookings(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String statusOrder,
            @RequestParam(required = false) String statusPayment,
            @RequestParam(required = false, defaultValue = "newest") String sort,
            @RequestParam(defaultValue = "1") int page,
            Model model) {

        int pageSize = 10;
        Page<BookingResponseDTO> bookingPage = adminBookingService.searchBookings(search, statusOrder, statusPayment, sort, page, pageSize);

        model.addAttribute("bookings", bookingPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", bookingPage.getTotalPages());

        // Giữ lại tham số tìm kiếm, lọc trên UI
        model.addAttribute("search", search);
        model.addAttribute("statusOrder", statusOrder);
        model.addAttribute("statusPayment", statusPayment);
        model.addAttribute("sort", sort);

        return "admin/bookings";
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<BookingResponseDTO> getBookingDetail(@PathVariable Integer id) {
        BookingResponseDTO dto = adminBookingService.getBookingDetail(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }
}
