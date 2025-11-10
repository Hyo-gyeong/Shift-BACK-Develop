package com.project.shift.product.controller;

import com.project.shift.product.dto.ReviewDTO;
import com.project.shift.product.service.IReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 리뷰 조회 API 컨트롤러.
 * - 특정 상품(productId)에 대한 리뷰 목록을 반환.
 */
@RestController
@RequestMapping("/products")
public class ReviewController {

    private final IReviewService reviewService;

    public ReviewController(IReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * [PROD-008] 특정 상품 리뷰 목록 조회
     * @param productId 리뷰를 조회할 상품 ID
     * @return 리뷰 목록 (최신 작성일 순)
     */
    @GetMapping("/{productId}/reviews")
    public ResponseEntity<List<ReviewDTO>> getProductReviews(@PathVariable Long productId) {
        List<ReviewDTO> reviews = reviewService.getReviewsByProductId(productId);
        if (reviews.isEmpty()) {
            return ResponseEntity.notFound().build(); // 404 반환
        }
        return ResponseEntity.ok(reviews);
    }
}