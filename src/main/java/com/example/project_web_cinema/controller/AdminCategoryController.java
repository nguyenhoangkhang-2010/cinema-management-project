package com.example.project_web_cinema.controller;

import com.example.project_web_cinema.dto.admin.CategoryDTO;
import com.example.project_web_cinema.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public String listCategories(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int page,
            Model model) {
        model.addAttribute("categories", categoryService.searchCategories(search, page, 10));
        model.addAttribute("search", search);
        model.addAttribute("currentPage", page);

        if (!model.containsAttribute("categoryDTO")) {
            model.addAttribute("categoryDTO", new CategoryDTO());
        }
        return "admin/categories";
    }

    @PostMapping("/save")
    public String saveCategory(@Valid @ModelAttribute("categoryDTO") CategoryDTO categoryDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng nhập tên thể loại hợp lệ!");
            return "redirect:/admin/categories";
        }
        try {
            if (categoryDTO.getMaTheLoai() != null) {
                categoryService.updateCategory(categoryDTO.getMaTheLoai(), categoryDTO);
                redirectAttributes.addFlashAttribute("success", "Cập nhật thành công!");
            } else {
                categoryService.createCategory(categoryDTO);
                redirectAttributes.addFlashAttribute("success", "Thêm thể loại thành công!");
            }
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/categories";
    }

    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            categoryService.deleteCategory(id);
            redirectAttributes.addFlashAttribute("success", "Đã xóa thể loại thành công!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/categories";
    }
}