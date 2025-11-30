package com.project.shift.product.service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.shift.product.dao.IReviewDAO;
import com.project.shift.product.dto.ReviewDTO;
import com.project.shift.product.dto.ReviewOriginDTO;
import com.project.shift.product.dto.UserReviewDetailDTO;
import com.project.shift.product.dto.UserReviewDetailProjection;
import com.project.shift.product.entity.Review;
import com.project.shift.product.entity.ReviewOriginEntity;
import com.project.shift.product.repository.ReviewEntityRepository;
import com.project.shift.user.dao.IUserDAO;

import lombok.RequiredArgsConstructor;

/**
 * [SERVICE-003] 리뷰 관련 비즈니스 로직 처리 클래스
 * ---------------------------------------------------------
 * - PROD-008 : 특정 상품 리뷰 조회
 * ---------------------------------------------------------
 * ※ 리뷰 작성자(UserEntity)와 조인하여 userName 반환
 */
@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewService {

    private final IReviewDAO reviewDAO;
    private final IUserDAO userDAO;
    private final ReviewEntityRepository reviewEntityRepository;


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

    /** [PROD-009] 특정 사용자가 작성한 모든 리뷰 목록 조회 (최신 작성일 순) */
	@Override
	@Transactional(readOnly = true)
	public List<UserReviewDetailDTO> getUserReviewDetails(Long userId) {
		List<UserReviewDetailProjection> reviewDetails = reviewDAO.findUserReviewDetails(userId);
		return reviewDetails.stream()
		        .map(p -> UserReviewDetailDTO.builder()
		                .reviewId(p.getReviewId())
		                .rating(p.getRating())
		                .content(p.getContent())
		                .createdDate(p.getCreatedDate())
		                .quantity(p.getQuantity())
		                .itemPrice(p.getItemPrice())
		                .productName(p.getProductName())
		                .price(p.getPrice())
		                .seller(p.getSeller())
		                .imageUrl(p.getImageUrl())
		                .build())
		        .toList();
	}

	/** [PROD-010] 리뷰 작성 */
	@Override
	@Transactional
	public void createReview(ReviewOriginDTO dto) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getName());
        
		dto.setCreatedDate(new Date());
    	dto.setUserId(userId);
    	
		reviewDAO.saveReview(ReviewOriginEntity.toEntity(dto));		
	}

	/** [PROD-011] 리뷰 삭제 */
	@Override
	@Transactional
	public void deleteReview(Long reviewId) {
		reviewDAO.deleteReview(reviewId);
	}

	/** [PROD-012] 리뷰 수정 */
	@Override
	@Transactional
	public void updateReview(ReviewOriginDTO dto) {
		ReviewOriginEntity entity = reviewEntityRepository.findById(dto.getReviewId())
		        .orElseThrow(() -> new RuntimeException("리뷰가 존재하지 않습니다."));
		
		entity.setContent(dto.getContent());
		entity.setCreatedDate(new Date());
		entity.setRating(dto.getRating());
		
		reviewDAO.saveReview(entity);
	}
}