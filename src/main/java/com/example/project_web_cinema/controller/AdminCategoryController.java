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
        return "admin/categories";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        if (!model.containsAttribute("categoryDTO")) {
            model.addAttribute("categoryDTO", new CategoryDTO());
        }
        return "admin/category-add";
    }

    @PostMapping("/add")
    public String createCategory(@Valid @ModelAttribute("categoryDTO") CategoryDTO categoryDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/category-add";
        }
        try {
            categoryService.createCategory(categoryDTO);
            redirectAttributes.addFlashAttribute("success", "Thêm thể loại thành công!");
            return "redirect:/admin/categories";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("categoryDTO", categoryDTO);
            return "redirect:/admin/categories/add";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        model.addAttribute("categoryDTO", categoryService.getCategoryById(id));
        return "admin/category-edit";
    }

    @PostMapping("/edit/{id}")
    public String updateCategory(@PathVariable Integer id,
            @Valid @ModelAttribute("categoryDTO") CategoryDTO categoryDTO, BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors())
            return "admin/category-edit";
        try {
            categoryService.updateCategory(id, categoryDTO);
            redirectAttributes.addFlashAttribute("success", "Cập nhật thành công!");
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