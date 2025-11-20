package com.project.shift.shop.controller;

import com.project.shift.shop.dto.*;
import com.project.shift.shop.service.ICartService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@RestController
@RequestMapping("/cart")
public class CartController {

    private final ICartService cartService;

    public CartController(ICartService cartService) {
        this.cartService = cartService;
    }
    //jwt 유틸
    private Long currentUserIdOrThrow() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new UnauthorizedException("인증 토큰이 필요합니다.");
        }
        try {
            return Long.parseLong(auth.getName());
        } catch (NumberFormatException e) {
            throw new UnauthorizedException("유효하지 않은 사용자 식별자입니다.");
        }
    }
    
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    private static class UnauthorizedException extends RuntimeException {
        public UnauthorizedException(String message) { super(message); }
    }
    
    
    // SHOP-001
    @GetMapping
    public CartResponseDTO getCart() {
        Long userId = currentUserIdOrThrow();
        return cartService.getCartByUserId(userId);
    }

    // SHOP-002
    @PostMapping
    public CartAddResponseDTO addCartItem(@RequestBody CartAddRequestDTO request) {
    	Long userId = currentUserIdOrThrow();

        if (request.getUserId() != null && !userId.equals(request.getUserId())) {
            throw new UnauthorizedException("요청의 userId가 토큰의 사용자와 일치하지 않습니다.");
        }

        var saved = cartService.addCartItem(userId, request.getProductId(), request.getQuantity());

        CartAddResponseDTO res = new CartAddResponseDTO();
        res.setCartId(saved.getCartId());
        res.setProductId(saved.getProductId());
        res.setQuantity(saved.getQuantity());
        res.setResult(true);
        return res;
    }

    // SHOP-003
    @PostMapping("/{cartId}")
    public CartUpdateResponseDTO updateCartItem(
            @PathVariable Long cartId,
            @RequestBody CartUpdateRequestDTO request
    ) {
        return cartService.updateCartItem(cartId, request.getQuantity());
    }
    // SHOP-004
    // DELETE /cart/{cartId}
    @DeleteMapping("/{cartId}")
    public CartDeleteResponseDTO deleteCartItem(@PathVariable Long cartId) {
        return cartService.deleteCartItem(cartId);
    }

    // SHOP-005
    // DELETE /cart/all  + body: { "userId": 3 }
    @DeleteMapping("/all")
    public CartClearResponseDTO clearCart() {
        Long userId = currentUserIdOrThrow();
        return cartService.clearCartByUserId(userId);
    }
}
