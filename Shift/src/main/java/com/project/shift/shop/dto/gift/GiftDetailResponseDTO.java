package com.project.shift.shop.dto.gift;

import lombok.*;
import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GiftDetailResponseDTO {
    private Long orderId;
    private String imageUrl;
    private String productName;
    private String senderName;
    private String orderStatus; // 주문 상태
    private String orderDate;
    private String deliveryStatus; // 배송 상태
    private Integer quantity;
    private String deliveryAddress;
    private Long productId;  
    private Long orderItemId;
    private List<GiftItemDTO> items;
}
