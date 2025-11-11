package com.project.shift.product.dao;

import com.project.shift.product.entity.Review;
import java.util.List;

/**
 * [DAO-INT-004] 리뷰 DAO 인터페이스
 * ---------------------------------------------------------
 * - ReviewDAO의 메서드 시그니처 정의
 */
public interface IReviewDAO {

    /** [PROD-008] 특정 상품 리뷰 목록 조회 (최신 작성일 순 정렬) */
    List<Review> findReviewsByProductId(Long productId);
}