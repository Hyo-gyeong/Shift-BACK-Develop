package com.project.shift.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {

    private Long cartId;       // cart.cart_items_id
    private Long productId;    // cart.product_id
    private String productName; // products.product_name
    private Integer quantity;   // cart.quantity
    private Integer price;      // cart.price (또는 products.price)
    private String imageUrl;    // 대표 이미지 (없으면 null)
}
