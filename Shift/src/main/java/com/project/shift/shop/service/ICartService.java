package com.project.shift.shop.service;

import com.project.shift.shop.dto.CartItemDTO;
import com.project.shift.shop.dto.CartResponseDTO;

public interface ICartService {

    // SHOP-001: userId로 장바구니 목록 조회
    CartResponseDTO getCartByUserId(Long userId);

    // SHOP-002: 장바구니에 상품 추가
    CartItemDTO addCartItem(Long userId, Long productId, Integer quantity);
}
