package com.project.shift.shop.service;

import com.project.shift.shop.dao.CartDAO; 
import com.project.shift.shop.dto.CartItemDTO;
import com.project.shift.shop.dto.CartResponseDTO;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CartService implements ICartService {

    private final CartDAO cartDAO;

    public CartService(CartDAO cartDAO) {
        this.cartDAO = cartDAO;
    }

    @Override
    public CartResponseDTO getCartByUserId(Long userId) {
        List<CartItemDTO> items = cartDAO.findByUserId(userId); 
        CartResponseDTO response = new CartResponseDTO();
        response.setItems(items);
        return response;
    }
}
