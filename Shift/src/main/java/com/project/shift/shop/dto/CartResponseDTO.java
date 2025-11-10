package com.project.shift.shop.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class CartResponseDTO {
    private List<CartItemDTO> items;
}
