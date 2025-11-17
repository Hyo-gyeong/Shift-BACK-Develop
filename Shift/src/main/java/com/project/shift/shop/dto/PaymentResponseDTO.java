package com.project.shift.shop.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PaymentResponseDTO {

    //private String paymentId;

    private Long orderId;
    private Integer cashAmount;   // 실제 현금 사용액
    private Integer pointUsed;    // 실제 포인트 사용액
    private String status;        
    private LocalDateTime approvedAt; // 결제 승인 시각
}
