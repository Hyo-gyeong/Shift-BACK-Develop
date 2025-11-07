package com.project.shift.shop.service;

import com.project.shift.shop.dto.CartResponseDTO;

public interface ICartService {

    // SHOP-001
    CartResponseDTO getCartByUserId(Long userId);
}
