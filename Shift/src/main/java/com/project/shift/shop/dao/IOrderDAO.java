package com.project.shift.shop.dao;

import com.project.shift.shop.entity.Order;
import java.util.Optional;
import java.util.List;


public interface IOrderDAO {
    Order save(Order order);
    Optional<Order> findById(Long orderId);
    List<Order> findBySenderId(Long senderId);
    // SHOP-009 결제 요청
    
    // SHOP-010 결제 결과 조회
    
    // SHOP-011 포인트 사용/적립 내역 조회
    
    // SHOP-012 주문 취소
    
    // SHOP-016 금액권 주문 생성
    
    // SHOP-017 금액권 결제 완료 (포인트 적립)
    
}
