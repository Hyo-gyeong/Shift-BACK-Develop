package com.project.shift.product.dto;

import lombok.*;

/**
 * 상품 이미지 DTO 클래스
 * - images 테이블 구조에 맞춰 작성
 * - 대표 이미지 여부(Y/N)를 포함
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageDTO {

    /** 이미지 ID (IMAGE_ID) */
    private Long imageId;

    /** 상품 ID (PRODUCT_ID) */
    private Long productId;

    /** 이미지 URL (IMAGE_URL) */
    private String imageUrl;

    /** 대표 이미지 여부 (IS_REPRESENTATIVE, 'Y' or 'N') */
    private String isRepresentative;
}