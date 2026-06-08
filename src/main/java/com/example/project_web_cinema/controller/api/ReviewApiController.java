package com.example.project_web_cinema.controller.api;

import com.example.project_web_cinema.dto.user.ReviewRequestDTO;
import com.example.project_web_cinema.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewApiController {

    private final ReviewService reviewService;

    @PostMapping("/submit")
    public ResponseEntity<?> submitReview(@RequestBody ReviewRequestDTO request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
            return ResponseEntity.status(401)
                    .body(Map.of("success", false, "message", "Vui lòng đăng nhập để đánh giá!"));
        }
        try {
            reviewService.saveReview(auth.getName(), request);
            return ResponseEntity.ok(Map.of("success", true, "message", "Đánh giá của bạn đã được ghi nhận!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<?> getReviews(@PathVariable Integer movieId) {
        return ResponseEntity.ok(reviewService.getReviewsByMovie(movieId));
    }
}