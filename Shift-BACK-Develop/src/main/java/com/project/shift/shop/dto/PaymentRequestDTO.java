package com.project.shift.shop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequestDTO {

    private Long orderId;      // 주문 번호
    private Integer amount;    // 결제 총액 (주문 totalPrice와 같아야 함)
    private Integer pointUsed; // 사용 포인트 (null 또는 0이면 포인트 미사용)
}
