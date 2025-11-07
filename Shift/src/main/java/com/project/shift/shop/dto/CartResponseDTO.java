package com.project.shift.shop.dto;

import lombok.Data;
import java.util.List;

@Data
public class CartResponseDTO {
    private List<CartItemDTO> items;
}
