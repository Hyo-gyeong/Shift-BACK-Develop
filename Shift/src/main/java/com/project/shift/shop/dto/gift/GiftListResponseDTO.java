package com.project.shift.shop.dto.gift;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class GiftListResponseDTO {
    private Long orderId;
    private String imageUrl;
    private String productName;
    private String senderName;
    private LocalDateTime orderDate;
    private String giftType;
}
