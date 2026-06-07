package com.example.project_web_cinema.controller.admin;

import com.example.project_web_cinema.dto.admin.SeatTypeDTO;
import com.example.project_web_cinema.service.admin.AdminSeatTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/seat-types")
@RequiredArgsConstructor
public class AdminSeatTypeController {

    private final AdminSeatTypeService seatTypeService;

    @GetMapping
    public String listSeatTypes(Model model) {
        model.addAttribute("seatTypes", seatTypeService.getAllSeatTypes());
        if (!model.containsAttribute("seatTypeDTO")) {
            model.addAttribute("seatTypeDTO", new SeatTypeDTO());
        }
        return "admin/seat-types";
    }

    @PostMapping("/save")
    public String saveSeatType(@Valid @ModelAttribute("seatTypeDTO") SeatTypeDTO seatTypeDTO, BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng nhập đầy đủ thông tin hợp lệ.");
            return "redirect:/admin/seat-types";
        }
        try {
            seatTypeService.saveSeatType(seatTypeDTO);
            redirectAttributes.addFlashAttribute("success", "Lưu loại ghế thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Tên loại ghế đã tồn tại hoặc có lỗi xảy ra.");
        }
        return "redirect:/admin/seat-types";
    }

    @GetMapping("/delete/{id}")
    public String deleteSeatType(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            seatTypeService.deleteSeatType(id);
            redirectAttributes.addFlashAttribute("success", "Đã xóa loại ghế!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Không thể xóa loại ghế đang được sử dụng trong phòng chiếu.");
        }
        return "redirect:/admin/seat-types";
    }
}
