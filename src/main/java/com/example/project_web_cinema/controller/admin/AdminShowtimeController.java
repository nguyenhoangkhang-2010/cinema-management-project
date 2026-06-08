package com.example.project_web_cinema.controller.admin;

import com.example.project_web_cinema.dto.admin.ShowtimeRequestDTO;
import com.example.project_web_cinema.service.admin.AdminShowtimeService;
import com.example.project_web_cinema.repository.MovieRepository;
import com.example.project_web_cinema.repository.RoomRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;
import java.time.LocalDate;

@Controller
@RequestMapping("/admin/showtimes")
public class AdminShowtimeController {
    private final AdminShowtimeService showtimeService;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;

    public AdminShowtimeController(AdminShowtimeService showtimeService, MovieRepository movieRepository,
            RoomRepository roomRepository) {
        this.showtimeService = showtimeService;
        this.movieRepository = movieRepository;
        this.roomRepository = roomRepository;
    }

    @GetMapping
    public String listShowtimes(@RequestParam(required = false) String search,
            @RequestParam(required = false) LocalDate ngayChieu,
            @RequestParam(defaultValue = "1") int page, Model model) {
        model.addAttribute("showtimes", showtimeService.searchShowtimes(search, ngayChieu,
                PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "ngayChieu", "gioBatDau"))));
        model.addAttribute("search", search);
        model.addAttribute("ngayChieu", ngayChieu);

        if (!model.containsAttribute("showtime")) {
            model.addAttribute("showtime", new ShowtimeRequestDTO());
        }
        model.addAttribute("movies", movieRepository.findAll());
        model.addAttribute("rooms", roomRepository.findAll());

        return "admin/showtimes";
    }

    @PostMapping("/save")
    public String saveShowtime(@RequestParam(required = false) Integer maSuatChieu,
            @Valid @ModelAttribute("showtime") ShowtimeRequestDTO dto,
            BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng nhập đầy đủ thông tin hợp lệ!");
            return "redirect:/admin/showtimes";
        }
        try {
            if (maSuatChieu != null) {
                showtimeService.updateShowtime(maSuatChieu, dto);
                redirectAttributes.addFlashAttribute("success", "Cập nhật suất chiếu thành công!");
            } else {
                showtimeService.addShowtime(dto);
                redirectAttributes.addFlashAttribute("success", "Thêm suất chiếu thành công!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/showtimes";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            showtimeService.deleteShowtime(id);
            redirectAttributes.addFlashAttribute("success", "Xóa suất chiếu thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/showtimes";
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public ShowtimeRequestDTO getShowtimeApi(@PathVariable Integer id) {
        return showtimeService.getShowtimeRequestDTO(id);
    }
}