package com.project.shift.shop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartAddRequestDTO {
    private Long userId;
    private Long productId;
    private Integer quantity;
}
