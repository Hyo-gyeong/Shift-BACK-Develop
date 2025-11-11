package com.project.shift.product.controller;

import com.project.shift.product.dto.ReviewDTO;
import com.project.shift.product.service.IReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * [CTRL-004] 리뷰 관련 API 컨트롤러
 * ---------------------------------------------------------
 * - PROD-008 : 특정 상품 리뷰 목록 조회
 */
@RestController
@RequestMapping("/products")
public class ReviewController {

    private final IReviewService reviewService;

    public ReviewController(IReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /** [PROD-008] 특정 상품 리뷰 목록 조회 (최신 작성일 순) */
    @GetMapping("/{productId}/reviews")
    public ResponseEntity<List<ReviewDTO>> getProductReviews(@PathVariable Long productId) {
        List<ReviewDTO> reviews = reviewService.getReviewsByProductId(productId);
        return reviews.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(reviews);
    }
}