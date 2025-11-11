package com.project.shift.product.dto;

import lombok.*;
import java.util.List;

/**
 * 상품 상세 및 목록 조회용 DTO
 * - PROD-001, PROD-002, PROD-004, PROD-009 공통 사용
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

    private String productId;       // 상품 ID
    private String productName;     // 상품명
    private int price;              // 가격
    private int stock;              // 재고
    private String registrationDate; // 등록 날짜
    private String seller;          // 판매자
    private List<String> imageUrls; // 상품 이미지 목록
    private String categoryName;    // 카테고리 이름
}