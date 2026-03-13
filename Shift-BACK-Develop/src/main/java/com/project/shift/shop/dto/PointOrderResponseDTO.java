package com.project.shift.shop.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointOrderResponseDTO { // SHOP-016 금액권 주문 생성

    private Long orderId;
    private Long senderId;
    private Long chatroomId;
    private Integer amount;
    private String status;   // CREATED / P / 등 주문 상태
    private boolean result;  // 성공 여부
}
