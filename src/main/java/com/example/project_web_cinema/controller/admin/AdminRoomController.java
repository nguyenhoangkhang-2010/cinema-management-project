package com.example.project_web_cinema.controller.admin;

import com.example.project_web_cinema.dto.admin.RoomDTO;
import com.example.project_web_cinema.repository.CinemaRepository;
import com.example.project_web_cinema.service.admin.AdminRoomService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/rooms")
public class AdminRoomController {

    private final AdminRoomService adminRoomService;
    private final CinemaRepository cinemaRepository;

    public AdminRoomController(AdminRoomService adminRoomService, CinemaRepository cinemaRepository) {
        this.adminRoomService = adminRoomService;
        this.cinemaRepository = cinemaRepository;
    }

    @GetMapping
    public String listRooms(@RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int page,
            Model model) {
        model.addAttribute("rooms", adminRoomService.searchRooms(search,
                PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "maPhong"))));
        model.addAttribute("search", search);
        model.addAttribute("cinemas", cinemaRepository.findAll()); // Dùng cho Select thả xuống (Dropdown)
        model.addAttribute("roomDTO", new RoomDTO());
        return "admin/rooms";
    }

    @PostMapping("/save")
    public String saveRoom(@ModelAttribute RoomDTO roomDTO, RedirectAttributes redirectAttributes) {
        try {
            adminRoomService.saveRoom(roomDTO);
            redirectAttributes.addFlashAttribute("success", "Lưu thông tin phòng chiếu thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/rooms";
    }

    @GetMapping("/delete/{id}")
    public String deleteRoom(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            adminRoomService.deleteRoom(id);
            redirectAttributes.addFlashAttribute("success", "Xóa phòng chiếu thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Không thể xóa phòng chiếu vì đã có dữ liệu liên kết (Suất chiếu/Ghế).");
        }
        return "redirect:/admin/rooms";
    }
}