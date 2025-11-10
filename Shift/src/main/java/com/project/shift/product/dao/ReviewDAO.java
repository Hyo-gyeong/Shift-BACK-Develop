package com.project.shift.product.dao;

import com.project.shift.product.entity.Review;
import com.project.shift.product.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * ReviewRepository를 이용해 데이터베이스와 직접 통신하는 DAO 클래스.
 */
@Repository
public class ReviewDAO implements IReviewDAO {

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewDAO(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    /** 상품 ID로 리뷰 목록 조회 (최신 작성일 순 정렬) */
    @Override
    public List<Review> findReviewsByProductId(Long productId) {
        return reviewRepository.findByProduct_IdOrderByCreatedDateDesc(productId);
    }
}