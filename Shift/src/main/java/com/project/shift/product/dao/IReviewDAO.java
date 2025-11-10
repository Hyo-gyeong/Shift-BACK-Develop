package com.project.shift.product.dao;

import com.project.shift.product.entity.Review;
import java.util.List;

/**
 * 리뷰 데이터 접근 계층 인터페이스.
 */
public interface IReviewDAO {
    List<Review> findReviewsByProductId(Long productId);
}