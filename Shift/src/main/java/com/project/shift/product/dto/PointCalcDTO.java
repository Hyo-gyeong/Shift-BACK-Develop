package com.project.shift.product.dto;

import lombok.*;

/**
 * 금액권 합계 계산 요청 및 응답에 사용되는 DTO 클래스
 * - 입력금액과 추가금액을 합산하고, 범위 검증 결과를 반환한다.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointCalcDTO {
    private int inputAmount;   // 사용자가 입력한 금액
    private int addAmount;     // 버튼 클릭으로 추가된 금액
    private int totalAmount;   // (inputAmount + addAmount)
    private boolean withinRange; // 유효 범위 여부
    private int minPrice;      // 최소 허용 금액
    private int maxPrice;      // 최대 허용 금액
}