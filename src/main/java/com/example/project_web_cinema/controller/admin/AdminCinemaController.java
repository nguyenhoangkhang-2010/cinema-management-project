package com.example.project_web_cinema.controller.admin;

import com.example.project_web_cinema.dto.admin.CinemaDTO;
import com.example.project_web_cinema.service.admin.AdminCinemaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/cinemas")
public class AdminCinemaController {

    private final AdminCinemaService adminCinemaService;

    public AdminCinemaController(AdminCinemaService adminCinemaService) {
        this.adminCinemaService = adminCinemaService;
    }

    @GetMapping
    public String listCinemas(@RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int page,
            Model model) {
        model.addAttribute("cinemas", adminCinemaService.searchCinemas(search,
                PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "maRap"))));
        model.addAttribute("search", search);
        model.addAttribute("cinemaDTO", CinemaDTO.builder().build()); // Form Object cho Modal thêm mới
        return "admin/cinemas";
    }

    @PostMapping("/save")
    public String saveCinema(@Valid @ModelAttribute("cinemaDTO") CinemaDTO cinemaDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error",
                    "Dữ liệu không hợp lệ! Vui lòng kiểm tra lại các trường đã nhập.");
            // Truyền lại DTO và lỗi để hiển thị trên modal
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.cinemaDTO", result);
            redirectAttributes.addFlashAttribute("cinema", cinemaDTO);
            return "redirect:/admin/cinemas";
        }
        try {
            adminCinemaService.saveCinema(cinemaDTO);
            redirectAttributes.addFlashAttribute("success", "Lưu thông tin rạp phim thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/cinemas";
    }

    @GetMapping("/delete/{id}")
    public String deleteCinema(@PathVariable Integer id) {
        adminCinemaService.deleteCinema(id);
        return "redirect:/admin/cinemas";
    }
}