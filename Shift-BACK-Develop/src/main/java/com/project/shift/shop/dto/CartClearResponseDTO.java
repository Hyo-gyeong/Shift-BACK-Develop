package com.project.shift.shop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartClearResponseDTO {
    private Long userId;
    private boolean cleared;
}
