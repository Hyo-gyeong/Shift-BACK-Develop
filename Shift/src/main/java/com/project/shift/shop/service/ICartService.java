package com.project.shift.shop.service;

import com.project.shift.shop.dto.*;

public interface ICartService {

    // SHOP-001
    CartResponseDTO getCartByUserId(Long userId);

    // SHOP-002
    CartItemDTO addCartItem(Long userId, Long productId, Integer quantity);

    // SHOP-003
    CartUpdateResponseDTO updateCartItem(Long cartId, Integer quantity);
}
