package com.project.shift.shop.service;

import com.project.shift.product.entity.Product;
import com.project.shift.product.repository.ProductRepository;
import com.project.shift.shop.dto.CartAddResponseDTO;
import com.project.shift.shop.dto.CartItemDTO;
import com.project.shift.shop.dto.CartResponseDTO;
import com.project.shift.shop.entity.Cart;
import com.project.shift.shop.repository.CartRepository;
import com.project.shift.user.entity.UserEntity;
import com.project.shift.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CartService implements ICartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository,
                       UserRepository userRepository,
                       ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public CartResponseDTO getCartByUserId(Long userId) {
        List<Cart> carts = cartRepository.findByUser_UserIdOrderByIdDesc(userId);

        CartResponseDTO response = new CartResponseDTO();
        response.setItems(
                carts.stream()
                        .map(this::toDto)
                        .toList()
        );
        return response;
    }

    @Override
    @Transactional
    public CartItemDTO addCartItem(Long userId, Long productId, Integer quantity) {

        // 1) 유저/상품 조회
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

        // 2) 엔티티 생성
        Cart cart = Cart.builder()
                .user(user)
                .product(product)
                .quantity(quantity)
                .price(product.getPrice())   // 담을 때 가격
                .build();

        // 3) 저장
        Cart saved = cartRepository.save(cart);

        // 4) DTO로 변환해서 반환
        return toDto(saved);
    }

    private CartItemDTO toDto(Cart entity) {
        CartItemDTO dto = new CartItemDTO();
        dto.setCartId(entity.getId());
        dto.setProductId(entity.getProduct().getId());
        dto.setProductName(entity.getProduct().getName());
        dto.setQuantity(entity.getQuantity());
        dto.setPrice(entity.getPrice());
        dto.setImageUrl(null);  // null, 추후
        return dto;
    }
}
