package com.project.shift.shop.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PaymentResultDTO {

    private Long orderId;
    private Integer cashAmount;   // orders.CASH_USED
    private Integer pointUsed;    // orders.POINT_USED
    private String status;        
    private LocalDateTime approvedAt; // 결제 시각
}
