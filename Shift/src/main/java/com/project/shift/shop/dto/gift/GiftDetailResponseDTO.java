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
    private String senderName;
    private String orderStatus; // 주문 상태
    private String deliveryStatus; // 배송 상태
    private Integer quantity;
    private String deliveryAddress;
}
