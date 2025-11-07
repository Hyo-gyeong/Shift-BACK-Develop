package com.project.shift.shop.controller;

import com.project.shift.shop.dto.CartResponseDTO;
import com.project.shift.shop.service.ICartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final ICartService cartService;

    public CartController(ICartService cartService) {
        this.cartService = cartService;
    }

    // SHOP-001 : 장바구니 목록 조회
    @GetMapping
    public CartResponseDTO getCart(@RequestParam("userId") Long userId) {
        return cartService.getCartByUserId(userId);
    }
}
