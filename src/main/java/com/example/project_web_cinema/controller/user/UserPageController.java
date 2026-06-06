package com.example.project_web_cinema.controller.user;

import com.example.project_web_cinema.service.UserStoreService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserPageController {

    private final UserStoreService userStoreService;

    @Autowired
    public UserPageController(UserStoreService userStoreService) {
        this.userStoreService = userStoreService;
    }

    @GetMapping({ "/", "/home" })
    public String home(Model model) {
        model.addAttribute("homeData", userStoreService.getHomepageData());
        return "user/home";
    }

    @GetMapping("/store")
    public String store(@RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int page, Model model) {
        if (search != null && !search.trim().isEmpty()) {
            model.addAttribute("storePage", userStoreService.searchMovie(search, PageRequest.of(page - 1, 8)));
            model.addAttribute("search", search);
        } else {
            model.addAttribute("storePage", userStoreService.getNowShowingMovies(PageRequest.of(page - 1, 8)));
        }
        return "user/store";
    }

    @GetMapping("/events")
    public String events(Model model) {
        model.addAttribute("featuredPromos", userStoreService.getFeaturedPromotions());
        model.addAttribute("activePromos", userStoreService.getActivePromotions());
        model.addAttribute("sneakPeek", userStoreService.getUpcomingMovies());
        return "user/events";
    }

    @GetMapping("/movie/{id}")
    public String movieDetail(@PathVariable Integer id, Model model) {
        model.addAttribute("movie", userStoreService.getMovieDetail(id));
        return "user/movie-detail";
    }

    @GetMapping("/promotion/{id}")
    public String promotionDetail(@PathVariable Integer id, Model model) {
        model.addAttribute("promotion", userStoreService.getPromotionDetail(id));
        return "user/promotion-detail";
    }
}