package com.project.shift.shop.repository;

import com.project.shift.shop.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface CartRepository extends JpaRepository<Cart, Long> {

    // SHOP-001: 특정 사용자 장바구니 전체
    List<Cart> findByUser_UserIdOrderByIdDesc(Long userId);

    // SHOP-005: 특정 사용자 장바구니 전체 삭제
    @Modifying
    @Query("DELETE FROM Cart c WHERE c.user.userId = :userId")
    int deleteAllByUser_UserId(@Param("userId") Long userId);
}
