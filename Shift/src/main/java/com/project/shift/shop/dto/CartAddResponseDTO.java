package com.project.shift.shop.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CartAddResponseDTO {
    private Long cartId;
    private Long productId;
    private Integer quantity;
    private boolean result;
}
