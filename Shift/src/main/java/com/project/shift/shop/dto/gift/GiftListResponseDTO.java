package com.project.shift.shop.dto.gift;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class GiftListResponseDTO {
    Long orderId;
    String imageUrl;
    String productName;
    String senderName; // 보낸 선물일 때 null 처리
    String receiverName; // 받은 선물일 때 null 처리
    String status;
    LocalDateTime orderDate;
    String giftType;
}
