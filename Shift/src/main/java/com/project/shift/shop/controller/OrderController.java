package com.project.shift.shop.controller;

import com.project.shift.shop.dto.OrderDTO;
import com.project.shift.shop.dto.OrderListResponseDTO;
import com.project.shift.shop.dto.OrderCancelResponseDTO;
import com.project.shift.shop.dto.OrderDetailResponseDTO;
import com.project.shift.shop.service.IOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class OrderController {

    private final IOrderService orderService;

    public OrderController(IOrderService orderService) {
        this.orderService = orderService;
    }
    
    // SHOP-006 주문 생성
    @PostMapping("/orders")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO request) {
        OrderDTO result = orderService.createOrder(request);
        return ResponseEntity.ok(result);
    }
    
    //SHOP-007 주문 내역 조회
    @GetMapping("/orders")
    public ResponseEntity<OrderListResponseDTO> getOrders(@RequestParam("userId") Long userId) {
        OrderListResponseDTO response = orderService.getOrdersByUser(userId);
        return ResponseEntity.ok(response);
    }
    
    // SHOP-008	주문 상세 조회
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderDetailResponseDTO> getOrderDetail(@PathVariable Long orderId) {
        OrderDetailResponseDTO response = orderService.getOrderDetail(orderId);
        return ResponseEntity.ok(response);
    }
    
    // SHOP-009 결제 요청
    
    // SHOP-010 결제 결과 조회
    
    // SHOP-011 포인트 사용/적립 내역 조회
    
    // SHOP-012 주문 취소
    @PutMapping("/orders/{orderId}/cancel")
    public ResponseEntity<OrderCancelResponseDTO> cancelOrder(@PathVariable("orderId") Long orderId) {
        OrderCancelResponseDTO response = orderService.cancelOrder(orderId);
        return ResponseEntity.ok(response);
    }
    // SHOP-016 금액권 주문 생성
    
    // SHOP-017 금액권 결제 완료 (포인트 적립)
}
