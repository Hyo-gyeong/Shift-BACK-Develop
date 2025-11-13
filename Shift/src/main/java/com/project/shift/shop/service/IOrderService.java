package com.project.shift.shop.service;

import com.project.shift.shop.dto.OrderDTO;
import com.project.shift.shop.dto.OrderListResponseDTO;
import com.project.shift.shop.dto.OrderDetailResponseDTO;



public interface IOrderService {
    OrderDTO createOrder(OrderDTO orderDTO);
    OrderListResponseDTO getOrdersByUser(Long userId);
    
    // SHOP-008
    OrderDetailResponseDTO getOrderDetail(Long orderId);
    
    // SHOP-009 결제 요청
    
    // SHOP-010 결제 결과 조회
    
    // SHOP-011 포인트 사용/적립 내역 조회
    
    // SHOP-012 주문 취소
    
    // SHOP-016 금액권 주문 생성
    
    // SHOP-017 금액권 결제 완료 (포인트 적립)
}
