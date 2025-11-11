package com.project.shift.product.repository;

import com.project.shift.product.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * [REP-004] 리뷰 Repository
 * ---------------------------------------------------------
 * - PROD-008 : 특정 상품 리뷰 조회
 */
public interface ReviewRepository extends JpaRepository<Review, Long> {

    /** [PROD-008] 특정 상품 ID 기준 리뷰 조회 (최신 작성일 순) */
    List<Review> findByProduct_IdOrderByCreatedDateDesc(Long productId);
}