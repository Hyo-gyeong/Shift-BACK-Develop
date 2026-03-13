package com.project.shift.shop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartDeleteResponseDTO {
    private Long cartId;
    private boolean deleted;
}
