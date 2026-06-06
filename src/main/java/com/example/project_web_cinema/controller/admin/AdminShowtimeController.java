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
        return "admin/showtimes";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("showtime", new ShowtimeRequestDTO());
        model.addAttribute("movies", movieRepository.findAll());
        model.addAttribute("rooms", roomRepository.findAll());
        return "admin/showtime-add";
    }

    @PostMapping("/add")
    public String addSubmit(@Valid @ModelAttribute("showtime") ShowtimeRequestDTO dto, BindingResult result,
            Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("movies", movieRepository.findAll());
            model.addAttribute("rooms", roomRepository.findAll());
            return "admin/showtime-add";
        }
        try {
            showtimeService.addShowtime(dto);
            redirectAttributes.addFlashAttribute("success", "Thêm suất chiếu thành công!");
            return "redirect:/admin/showtimes";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("movies", movieRepository.findAll());
            model.addAttribute("rooms", roomRepository.findAll());
            return "admin/showtime-add";
        }
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        model.addAttribute("showtime", showtimeService.getShowtimeRequestDTO(id));
        model.addAttribute("movies", movieRepository.findAll());
        model.addAttribute("rooms", roomRepository.findAll());
        model.addAttribute("id", id);
        return "admin/showtime-edit";
    }

    @PostMapping("/edit/{id}")
    public String editSubmit(@PathVariable Integer id, @Valid @ModelAttribute("showtime") ShowtimeRequestDTO dto,
            BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("movies", movieRepository.findAll());
            model.addAttribute("rooms", roomRepository.findAll());
            return "admin/showtime-edit";
        }
        try {
            showtimeService.updateShowtime(id, dto);
            redirectAttributes.addFlashAttribute("success", "Cập nhật suất chiếu thành công!");
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
}