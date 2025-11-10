package com.project.shift.shop.service;

import com.project.shift.shop.dao.CartDAO;
import com.project.shift.shop.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class CartService implements ICartService {

    private final CartDAO cartDAO;

    public CartService(CartDAO cartDAO) {
        this.cartDAO = cartDAO;
    }

    // SHOP-001
    @Override
    public CartResponseDTO getCartByUserId(Long userId) {
        List<CartItemDTO> items = cartDAO.findByUserId(userId);
        CartResponseDTO dto = new CartResponseDTO();
        dto.setItems(items);
        return dto;
    }

    // SHOP-002
    @Override
    public CartItemDTO addCartItem(Long userId, Long productId, Integer quantity) {
        return cartDAO.insertCartItem(userId, productId, quantity);
    }

    // SHOP-003
    @Override
    public CartUpdateResponseDTO updateCartItem(Long cartId, Integer quantity) {
        var updated = cartDAO.updateCartItemQuantity(cartId, quantity);

        CartUpdateResponseDTO dto = new CartUpdateResponseDTO();
        dto.setCartId(updated.getCartId());
        dto.setProductId(updated.getProductId());
        dto.setQuantity(updated.getQuantity());
        dto.setTotalPrice(updated.getPrice() * updated.getQuantity());
        dto.setResult(true);
        return dto;
    }
    
    // SHOP-004
    @Override
    public CartDeleteResponseDTO deleteCartItem(Long cartId) {
        boolean deleted = cartDAO.deleteCartItem(cartId);

        CartDeleteResponseDTO dto = new CartDeleteResponseDTO();
        dto.setCartId(cartId);
        dto.setDeleted(deleted);
        return dto;
    }

    // SHOP-005
    @Override
    public CartClearResponseDTO clearCartByUserId(Long userId) {
        long deletedCount = cartDAO.clearCartByUserId(userId);

        CartClearResponseDTO dto = new CartClearResponseDTO();
        dto.setUserId(userId);
        dto.setCleared(deletedCount > 0);
        return dto;
    }
}
