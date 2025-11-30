package com.project.shift.product.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.shift.product.dto.ReviewDTO;
import com.project.shift.product.dto.UserReviewDetailDTO;
import com.project.shift.product.service.IReviewService;

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
    
    /** [PROD-009] 특정 사용자가 작성한 모든 리뷰 목록 조회 (최신 작성일 순) */
    @GetMapping("/users/reviews")
    public ResponseEntity<List<UserReviewDetailDTO>> getUserProductDetailReviews() {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getName());
        List<UserReviewDetailDTO> reviewDetails = reviewService.getUserReviewDetails(userId);
        return reviewDetails.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(reviewDetails);
    }

}