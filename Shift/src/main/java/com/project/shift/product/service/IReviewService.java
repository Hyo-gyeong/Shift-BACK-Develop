package com.project.shift.product.service;

import com.project.shift.product.dto.ReviewDTO;
import java.util.List;

/**
 * 리뷰 비즈니스 로직 인터페이스.
 */
public interface IReviewService {
    List<ReviewDTO> getReviewsByProductId(Long productId);
}