package com.project.shift.shop.repository;

import com.project.shift.shop.entity.Order;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findBySenderIdOrderByOrderDateDesc(Long senderId);
    
    // SHOP-009 결제 요청
    
    // SHOP-010 결제 결과 조회
    
    // SHOP-011 포인트 사용/적립 내역 조회
    
    // SHOP-012 주문 취소
    
    // SHOP-016 금액권 주문 생성
    
    // SHOP-017 금액권 결제 완료 (포인트 적립)
    @Modifying
    @Transactional
    @Query("""
        UPDATE Order o 
           SET o.orderStatus = :status,
               o.remainPoints = :remainPoints
         WHERE o.orderId = :orderId
    """)
    void updateOrderStatusAndPoints(
            @Param("orderId") Long orderId,
            @Param("status") String status,
            @Param("remainPoints") Integer remainPoints
    );
}
