package com.project.shift.shop.dto.gift;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GiftItemDetailDTO {
    private Long orderItemId;
    private Long productId;
    private String productName;
    private Integer quantity;
    private Integer price;
    private String imageUrl;
    private Long categoryId;   // 금액권(카테고리 3) 여부 판단용
    private Boolean reviewWritten;
}
