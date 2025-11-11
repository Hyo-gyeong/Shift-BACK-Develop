package com.project.shift.product.service;

import com.project.shift.product.dto.ReviewDTO;
import java.util.List;

/**
 * [I-SERVICE-003] 리뷰 관련 서비스 인터페이스
 * ---------------------------------------------------------
 * - PROD-008 : 특정 상품 리뷰 조회
 */
public interface IReviewService {

    /** [PROD-008] 특정 상품의 리뷰 목록 조회 */
    List<ReviewDTO> getReviewsByProductId(Long productId);
}