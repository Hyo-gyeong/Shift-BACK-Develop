package com.project.shift.shop.repository;

import com.project.shift.shop.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {

    // SHOP-001: 특정 사용자 장바구니 전체
    List<Cart> findByUser_UserIdOrderByIdDesc(Long userId);
}
