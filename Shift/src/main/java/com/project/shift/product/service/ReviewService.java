package com.project.shift.product.service;

import com.project.shift.product.dao.IReviewDAO;
import com.project.shift.product.dto.ReviewDTO;
import com.project.shift.product.entity.Review;
import com.project.shift.user.dao.IUserDAO;
import com.project.shift.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 리뷰 관련 비즈니스 로직을 처리하는 서비스 클래스.
 * - 리뷰 조회 시 작성자 이름 포함.
 */
@Service
public class ReviewService implements IReviewService {

    private final IReviewDAO reviewDAO;
	private final IUserDAO userDAO;

    @Autowired
    public ReviewService(IReviewDAO reviewDAO, IUserDAO userDAO) {
        this.reviewDAO = reviewDAO;
        this.userDAO = userDAO;
    }

    /** 특정 상품의 리뷰 목록 조회 */
    @Override
    public List<ReviewDTO> getReviewsByProductId(Long productId) {
        List<Review> reviews = reviewDAO.findReviewsByProductId(productId);

        return reviews.stream().map(review -> ReviewDTO.builder()
                .reviewId(review.getId())
                .userName(userDAO.findById(review.getUserId())
                        .map(user -> user.getName())
                        .orElse("탈퇴한 사용자"))
                .rating(review.getRating())
                .content(review.getContent())
                .createdDate(review.getCreatedDate())
                .build()
        ).collect(Collectors.toList());
    }
}