package com.project.shift.product.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.project.shift.product.dto.UserReviewDetailProjection;
import com.project.shift.product.entity.Review;
import com.project.shift.product.entity.ReviewOriginEntity;
import com.project.shift.product.repository.ReviewEntityRepository;
import com.project.shift.product.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

/**
 * [DAO-004] 리뷰 관련 데이터 접근 클래스
 * ---------------------------------------------------------
 * - PROD-008 : 특정 상품 리뷰 조회 (최신 작성일 순)
 * ---------------------------------------------------------
 * ReviewRepository를 통해 DB와 직접 통신
 */
@Repository
@RequiredArgsConstructor
public class ReviewDAO implements IReviewDAO {

    private final ReviewRepository reviewRepository;
    private final ReviewEntityRepository reviewEntityRepository;

    /** [PROD-008] 상품 ID 기준 리뷰 목록 조회 (최신 작성일 순 정렬) */
    @Override
    public List<Review> findReviewsByProductId(Long productId) {
        return reviewRepository.findByProduct_IdOrderByCreatedDateDesc(productId);
    }

    /** [PROD-009] 특정 사용자가 작성한 모든 리뷰 목록 조회 (최신 작성일 순) */
	@Override
	public List<UserReviewDetailProjection> findUserReviewDetails(Long userId) {
		return reviewRepository.findUserReviewDetails(userId);
	}

	/** [PROD-010] 리뷰 작성 */
	@Override
	public void saveNewReview(ReviewOriginEntity entity) {
		reviewEntityRepository.save(entity);		
	}

	/** [PROD-011] 리뷰 삭제 */
	@Override
	public void deleteReview(Long reviewId) {
		reviewEntityRepository.deleteById(reviewId);
	}

	/** [PROD-012] 리뷰 수정 */
	@Override
	public void updateReview(ReviewOriginEntity entity) {
		reviewEntityRepository.save(entity);
	}
}