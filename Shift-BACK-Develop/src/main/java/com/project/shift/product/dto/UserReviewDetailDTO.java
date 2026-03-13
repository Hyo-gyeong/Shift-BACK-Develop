package com.project.shift.product.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 특정 사용자가 작성한 전체 리뷰 조회 응답 DTO.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserReviewDetailDTO {

    private long reviewId;
    private long rating;
    private String content;
    private Date createdDate;

    private int quantity;     // 주문한 수량
    private int itemPrice;    // 구매 당시 단가
    
    private Long productId;
    private Long orderItemId;

    private String productName;
    private int price;        // 상품 현재 가격
    private String seller;

    private String imageUrl;      // 대표 이미지 외의 이미지
}

