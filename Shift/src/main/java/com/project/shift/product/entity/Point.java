package com.project.shift.product.entity;

import lombok.*;

/**
 * 금액권(POINT) 조회용 DTO 성격 클래스.
 * 실제 DB 테이블은 존재하지 않으며,
 * PRODUCTS 테이블 내 category_id = 3 인 데이터를 참조함.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Point {

    /** 금액권 상품명 */
    private String productName;

    /** 금액권 이미지 */
    private String imageUrl;

    /** 금액권 설명 */
    private String description;

    /** 최소 금액 */
    private int minAmount;

    /** 최대 금액 */
    private int maxAmount;
}