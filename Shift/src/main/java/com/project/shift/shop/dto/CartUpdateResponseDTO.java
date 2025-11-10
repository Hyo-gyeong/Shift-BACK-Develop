package com.project.shift.shop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartUpdateResponseDTO {
    private Long cartId;
    private Long productId;
    private Integer quantity;
    private Integer totalPrice;
    private boolean result;
}
