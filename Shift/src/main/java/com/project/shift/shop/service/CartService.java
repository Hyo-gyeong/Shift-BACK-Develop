package com.project.shift.shop.service;

import com.project.shift.product.entity.Product;
import com.project.shift.product.repository.ProductRepository;
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

    // SHOP-001 
    @Override
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

    // SHOP-002
    @Override
    public CartItemDTO addCartItem(Long userId, Long productId, Integer quantity) {

        // 1) 유저 조회
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        // 2) 상품 조회
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

        // 3) 장바구니 엔티티 생성
        Cart cart = Cart.builder()
                .user(user)
                .product(product)
                .quantity(quantity)
                .price(product.getPrice())   // 단가 저장
                .build();

        // 4) 저장
        Cart saved = cartRepository.save(cart);

        // 5) 반환
        return toDto(saved);
    }

    // SHOP-003
    @Override
    public CartItemDTO updateCartQuantity(Long cartId, Integer quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found: " + cartId));

        // 수량만 바꿈 (단가는 담을 때 가격을 유지)
        cart.setQuantity(quantity);

        // 변경 내용 저장
        Cart updated = cartRepository.save(cart);

        return toDto(updated);
    }

    // 공통 변환 메서드
    private CartItemDTO toDto(Cart entity) {
        CartItemDTO dto = new CartItemDTO();
        dto.setCartId(entity.getId());
        dto.setProductId(entity.getProduct().getId());
        dto.setProductName(entity.getProduct().getName());
        dto.setQuantity(entity.getQuantity());
        dto.setPrice(entity.getPrice());  // 단가
        dto.setImageUrl(null);            // 추후 연동
        return dto;
    }
}
