package com.project.shift.product.dto;

import lombok.*;

/**
 * PROD-010 금액권 페이지 정보 응답 DTO
 * - 금액권의 고정 템플릿 정보를 담는다.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointDTO {
    private String productName;  // "포인트 금액권"
    private String imageUrl;     // 대표 이미지
    private String description;  // 금액권 안내 문구
    private int minAmount;       // 최소 입력 금액
    private int maxAmount;       // 최대 입력 금액
}