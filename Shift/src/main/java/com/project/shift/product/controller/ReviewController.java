package com.project.shift.product.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.shift.product.dto.ReviewOriginDTO;
import com.project.shift.product.dto.ReviewDTO;
import com.project.shift.product.dto.UserReviewDetailDTO;
import com.project.shift.product.service.IReviewService;

import lombok.RequiredArgsConstructor;

/**
 * [CTRL-004] 리뷰 관련 API 컨트롤러
 * ---------------------------------------------------------
 * - PROD-008 : 특정 상품 리뷰 목록 조회
 */
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ReviewController {

    private final IReviewService reviewService;
    
    /** [PROD-008] 특정 상품 리뷰 목록 조회 (최신 작성일 순) */
    @GetMapping("/{productId}/reviews")
    public ResponseEntity<List<ReviewDTO>> getProductReviews(@PathVariable Long productId) {
        List<ReviewDTO> reviews = reviewService.getReviewsByProductId(productId);
        
        // 리뷰가 없으면 빈 배열을 반환하도록 수정
        return reviews.isEmpty() ? ResponseEntity.ok(Collections.emptyList()) : ResponseEntity.ok(reviews);
    }
    
    /** [PROD-009] 특정 사용자가 작성한 모든 리뷰 목록 조회 (최신 작성일 순) */
    @GetMapping("/users/reviews")
    public ResponseEntity<List<UserReviewDetailDTO>> getUserProductDetailReviews() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getName());
        List<UserReviewDetailDTO> reviewDetails = reviewService.getUserReviewDetails(userId);
        
        // 리뷰가 없으면 빈 배열을 반환하도록 수정
        return reviewDetails.isEmpty() ? ResponseEntity.ok(Collections.emptyList()) : ResponseEntity.ok(reviewDetails);
    }
    
    /** [PROD-010] 리뷰 작성 */
    @PostMapping("/reviews")
    public void createReview(@RequestBody ReviewOriginDTO dto) {
    	reviewService.createReview(dto);
    }
    
    /** [PROD-011] 리뷰 삭제 */
    @DeleteMapping("/reviews/{reviewId}")
    public void deleteReview(@PathVariable Long reviewId){
    	reviewService.deleteReview(reviewId);
    }
    
    /** [PROD-012] 리뷰 수정 */
    @PatchMapping("/reviews")
    public void updateReview(@RequestBody ReviewOriginDTO dto){
    	reviewService.updateReview(dto);
    }
    
    /** [PROD-013] 리뷰 작성 여부 + 작성 가능 여부 확인 */
    @GetMapping("/order-items/{orderItemId}/reviews/check")
    public ResponseEntity<Map<String, Object>> checkReviewStatus(@PathVariable Long orderItemId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getName());

        Map<String, Object> result = reviewService.checkReviewStatus(userId, orderItemId);
        return ResponseEntity.ok(result);
    }

}