package com.project.shift.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {

    private Long cartId;
    private Long productId;
    private String productName;
    private Integer quantity;
    private Integer price;
    private String imageUrl;
}
