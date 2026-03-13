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
public class PointOrderCompleteDTO {

    private Long chatroomId;         // 금액권을 받은 채팅방
    private Long receiverId;         // 포인트 적립 대상 사용자
    private Integer addedPoints;     // 적립된 포인트 (금액권 금액)
    private Integer updatedTotalPoints; // 적립 이후 receiver의 총 포인트
    private boolean result;          // 성공 여부
}