package com.project.shift.shop.repository;

import com.project.shift.shop.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface CartRepository extends JpaRepository<Cart, Long> {

    // SHOP-001: 특정 사용자 장바구니 전체
    List<Cart> findByUser_UserIdOrderByIdDesc(Long userId);
    
    // 동일 사용자 + 동일 상품 존재 여부 확인 (중복 담기 시 수량 증가에 사용)
    Optional<Cart> findByUser_UserIdAndProduct_Id(Long userId, Long productId);

    // SHOP-005: 특정 사용자 장바구니 전체 삭제
    @Modifying
    @Query("DELETE FROM Cart c WHERE c.user.userId = :userId")
    int deleteAllByUser_UserId(@Param("userId") Long userId);
    
    //수량가산
    @Modifying
    @Query("""
           UPDATE Cart c
              SET c.quantity = c.quantity + :inc
            WHERE c.user.userId = :userId
              AND c.product.id = :productId
           """)
    int increaseQuantity(@Param("userId") Long userId,
                         @Param("productId") Long productId,
                         @Param("inc") Integer inc);
}
