package com.project.shift.product.service;

import com.project.shift.product.dao.IReviewDAO;
import com.project.shift.product.dto.ReviewDTO;
import com.project.shift.product.entity.Review;
import com.project.shift.user.dao.IUserDAO;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * [SERVICE-003] 리뷰 관련 비즈니스 로직 처리 클래스
 * ---------------------------------------------------------
 * - PROD-008 : 특정 상품 리뷰 조회
 * ---------------------------------------------------------
 * ※ 리뷰 작성자(UserEntity)와 조인하여 userName 반환
 */
@Service
public class ReviewService implements IReviewService {

    private final IReviewDAO reviewDAO;
    private final IUserDAO userDAO;

    public ReviewService(IReviewDAO reviewDAO, IUserDAO userDAO) {
        this.reviewDAO = reviewDAO;
        this.userDAO = userDAO;
    }

    /** [PROD-008] 특정 상품의 리뷰 목록 조회 */
    @Override
    public List<ReviewDTO> getReviewsByProductId(Long productId) {
        List<Review> reviews = reviewDAO.findReviewsByProductId(productId);
        if (reviews.isEmpty()) return Collections.emptyList();

        return reviews.stream().map(review -> ReviewDTO.builder()
                .reviewId(review.getId())
                .userName(userDAO.findById(review.getUser().getUserId())
                        .map(u -> u.getName())
                        .orElse("탈퇴한 사용자"))
                .rating(review.getRating())
                .content(review.getContent())
                .createdDate(review.getCreatedDate())
                .build())
                .collect(Collectors.toList());
    }
}