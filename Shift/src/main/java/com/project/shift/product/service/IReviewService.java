package com.project.shift.product.service;

import java.util.List;

import com.project.shift.product.dto.ReviewOriginDTO;
import com.project.shift.product.dto.ReviewDTO;
import com.project.shift.product.dto.UserReviewDetailDTO;

/**
 * [I-SERVICE-003] 리뷰 관련 서비스 인터페이스
 * ---------------------------------------------------------
 * - PROD-008 : 특정 상품 리뷰 조회
 */
public interface IReviewService {

    /** [PROD-008] 특정 상품의 리뷰 목록 조회 */
    List<ReviewDTO> getReviewsByProductId(Long productId);
    /** [PROD-009] 특정 사용자가 작성한 모든 리뷰 목록 조회 (최신 작성일 순) */
    List<UserReviewDetailDTO> getUserReviewDetails(Long userId);
    /** [PROD-010] 리뷰 작성 */
    void createReview(ReviewOriginDTO dto);
    /** [PROD-011] 리뷰 삭제 */
    void deleteReview(Long reviewId);
    /** [PROD-012] 리뷰 수정 */
    void updateReview(ReviewOriginDTO dto);
}