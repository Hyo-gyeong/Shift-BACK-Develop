package com.project.shift.product.dto;

import lombok.*;
import java.util.Date;

/**
 * 리뷰 조회 응답 DTO.
 * - 클라이언트로 전달할 리뷰 데이터 구조 정의.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDTO {
    private Long reviewId;       // 리뷰 ID
    private String userName;     // 작성자 이름
    private Integer rating;      // 평점 (1~5)
    private String content;      // 리뷰 내용
    private Date createdDate;    // 작성일
}