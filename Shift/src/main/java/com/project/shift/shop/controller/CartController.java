package com.project.shift.shop.controller;

import com.project.shift.shop.dto.CartResponseDTO;
import com.project.shift.shop.dto.CartItemDTO;
import com.project.shift.shop.dto.CartAddRequestDTO;
import com.project.shift.shop.dto.CartAddResponseDTO;
import com.project.shift.shop.service.ICartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final ICartService cartService;

    public CartController(ICartService cartService) {
        this.cartService = cartService;
    }

    // ===== SHOP-001 : 장바구니 목록 조회 =====
    @GetMapping
    public CartResponseDTO getCart(@RequestParam("userId") Long userId) {
        return cartService.getCartByUserId(userId);
    }

    // ===== SHOP-002 : 장바구니 상품 추가 =====
    @PostMapping
    public CartAddResponseDTO addCartItem(@RequestBody CartAddRequestDTO request) {

        CartItemDTO saved = cartService.addCartItem(
                request.getUserId(),
                request.getProductId(),
                request.getQuantity()
        );

        CartAddResponseDTO response = new CartAddResponseDTO();
        response.setCartId(saved.getCartId());
        response.setProductId(saved.getProductId());
        response.setQuantity(saved.getQuantity());
        response.setResult(true);

        return response;
    }
}
