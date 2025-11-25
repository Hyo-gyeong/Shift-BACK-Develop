package com.project.shift.shop.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointHistoryDTO {

    private Long transactionId;    // PK
    private String type;           // A / U / R
    private Integer amount;        // 금액권 금액, 사용/적립/복원 금액
    private LocalDateTime createdAt;
    private Long orderId;          // 일반 주문과 연결되었을 경우만 존재
}