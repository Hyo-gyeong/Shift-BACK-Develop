package com.project.shift.shop.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrderStatusUpdateResponseDTO {

    private Long orderId;
    private Long senderId;
    private Long receiverId;

    // 주문 상태: P / S / D / C
    private String orderStatus;

    // 배송 상태: P / S / D / C
    private String deliveryStatus;

    // 리뷰 버튼 활성화 여부 (주문상태/배송상태 둘 다 D일 때 true)
    private boolean reviewButtonEnabled;

    // 구매/수령 확정 시각 (선물수락 시에는 null)
    private LocalDateTime confirmedAt;

    private boolean result;
}
