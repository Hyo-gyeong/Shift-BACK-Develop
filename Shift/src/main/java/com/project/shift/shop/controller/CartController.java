package com.project.shift.shop.controller;

import com.project.shift.shop.dto.*;
import com.project.shift.shop.service.ICartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final ICartService cartService;

    public CartController(ICartService cartService) {
        this.cartService = cartService;
    }

    // SHOP-001
    @GetMapping
    public CartResponseDTO getCart(@RequestParam("userId") Long userId) {
        return cartService.getCartByUserId(userId);
    }

    // SHOP-002
    @PostMapping
    public CartAddResponseDTO addCartItem(@RequestBody CartAddRequestDTO request) {

        var saved = cartService.addCartItem(
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

    // SHOP-003 : 장바구니 수량 변경
    @PostMapping("/{cartId}")
    public CartUpdateResponseDTO updateCartItem(
            @PathVariable Long cartId,
            @RequestBody CartUpdateRequestDTO request
    ) {
        return cartService.updateCartItem(cartId, request.getQuantity());
    }
}
