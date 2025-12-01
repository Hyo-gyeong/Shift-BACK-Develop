package com.project.shift.shop.dto.gift;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GiftDetailResponseDTO {
    private Long orderId;
    private String imageUrl;
    private String productName;
    private String senderName; // 보낸 선물일 때 null 처리
    private String receiverName; // 받은 선물일 때 null 처리
    private String status; // 배송/주문 상태
    private Integer quantity;
    private String deliveryAddress;
}
