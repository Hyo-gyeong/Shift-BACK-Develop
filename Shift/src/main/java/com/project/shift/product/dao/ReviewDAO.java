package com.project.shift.product.dao;

import com.project.shift.product.entity.Review;
import com.project.shift.product.repository.ReviewRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * [DAO-004] 리뷰 관련 데이터 접근 클래스
 * ---------------------------------------------------------
 * - PROD-008 : 특정 상품 리뷰 조회 (최신 작성일 순)
 * ---------------------------------------------------------
 * ReviewRepository를 통해 DB와 직접 통신
 */
@Repository
public class ReviewDAO implements IReviewDAO {

    private final ReviewRepository reviewRepository;

    public ReviewDAO(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    /** [PROD-008] 상품 ID 기준 리뷰 목록 조회 (최신 작성일 순 정렬) */
    @Override
    public List<Review> findReviewsByProductId(Long productId) {
        return reviewRepository.findByProduct_IdOrderByCreatedDateDesc(productId);
    }
}