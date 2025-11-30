package com.project.shift.product.dao;

import java.util.List;

import com.project.shift.product.dto.UserReviewDetailProjection;
import com.project.shift.product.entity.Review;
import com.project.shift.product.entity.ReviewOriginEntity;

/**
 * [DAO-INT-004] 리뷰 DAO 인터페이스
 * ---------------------------------------------------------
 * - ReviewDAO의 메서드 시그니처 정의
 */
public interface IReviewDAO {

    /** [PROD-008] 특정 상품 리뷰 목록 조회 (최신 작성일 순 정렬) */
    List<Review> findReviewsByProductId(Long productId);
    /** [PROD-009] 특정 사용자가 작성한 모든 리뷰 목록 조회 (최신 작성일 순) */
    List<UserReviewDetailProjection> findUserReviewDetails(Long userId);
    /** [PROD-010] 리뷰 작성, 수정 */
    void saveReview(ReviewOriginEntity dto);
    /** [PROD-011] 리뷰 삭제 */
    void deleteReview(Long reviewId);
}