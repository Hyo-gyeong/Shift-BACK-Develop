package com.project.shift.shop.dao;

import com.project.shift.product.entity.Product;
import com.project.shift.product.repository.ProductRepository;
import com.project.shift.shop.dto.CartItemDTO;
import com.project.shift.shop.entity.Cart;
import com.project.shift.shop.repository.CartRepository;
import com.project.shift.user.entity.UserEntity;
import com.project.shift.user.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class CartDAO {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartDAO(CartRepository cartRepository,
                   UserRepository userRepository,
                   ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    // SHOP-001 : userId로 장바구니 조회
    public List<CartItemDTO> findByUserId(Long userId) {
        List<Cart> carts = cartRepository.findByUser_UserIdOrderByIdDesc(userId);
        return carts.stream()
                .map(this::toDto)
                .toList();
    }

    // SHOP-002 : 장바구니에 상품 추가
    public CartItemDTO insertCartItem(Long userId, Long productId, int quantity) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

        Cart cart = Cart.builder()
                .user(user)
                .product(product)
                .quantity(quantity)
                .price(product.getPrice()) 
                .build();

        Cart saved = cartRepository.save(cart);
        return toDto(saved);
    }

    // SHOP-003 : 장바구니 수량 변경
    public CartItemDTO updateCartItemQuantity(Long cartId, int quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found: " + cartId));

        cart.setQuantity(quantity);
        Cart saved = cartRepository.save(cart);

        return toDto(saved);
    }
    
    // SHOP-004 : 장바구니 상품 단건 삭제
    public boolean deleteCartItem(Long cartId) {
        boolean exists = cartRepository.existsById(cartId);
        if (!exists) {
            return false;
        }
        cartRepository.deleteById(cartId);
        return true;
    }

    // SHOP-005 : 해당 사용자의 장바구니 전체 비우기
    public long clearCartByUserId(Long userId) {
        return cartRepository.deleteAllByUser_UserId(userId);
    }
    
    // 내부 공통 변환
    private CartItemDTO toDto(Cart entity) {
        CartItemDTO dto = new CartItemDTO();
        dto.setCartId(entity.getId());
        dto.setProductId(entity.getProduct().getId());
        dto.setProductName(entity.getProduct().getName());
        dto.setQuantity(entity.getQuantity());
        dto.setPrice(entity.getPrice());
        dto.setImageUrl(null); 
        return dto;
    }
}
