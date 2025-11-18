package com.project.shift.shop.controller;

import com.project.shift.shop.dto.*;
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
    @PostMapping("/payments")
    public ResponseEntity<PaymentResponseDTO> requestPayment(@RequestBody PaymentRequestDTO request) {
        PaymentResponseDTO response = orderService.requestPayment(request);
        return ResponseEntity.ok(response);
    }
    // SHOP-010 결제 결과 조회
    @GetMapping("/payments/{orderId}")
    public ResponseEntity<PaymentResultDTO> getPaymentResult(@PathVariable Long orderId) {
        PaymentResultDTO response = orderService.getPaymentResult(orderId);
        return ResponseEntity.ok(response);
    }
    // SHOP-011 포인트 사용/적립 내역 조회
    
    // SHOP-012 주문 취소
    @PutMapping("/orders/{orderId}/cancel")
    public ResponseEntity<OrderCancelResponseDTO> cancelOrder(@PathVariable("orderId") Long orderId) {
        OrderCancelResponseDTO response = orderService.cancelOrder(orderId);
        return ResponseEntity.ok(response);
    }
    // SHOP-016 금액권 주문 생성
    @PostMapping("/orders/point")
    public ResponseEntity<PointOrderResponseDTO> createPointOrder(
            @RequestBody PointOrderRequestDTO request) {

        PointOrderResponseDTO response = orderService.createPointOrder(request);
        return ResponseEntity.ok(response);
    }
    // SHOP-017 금액권 결제 완료 (포인트 적립)
    @PutMapping("/orders/point/complete/{orderId}")
    public ResponseEntity<PointOrderCompleteDTO> completePointOrder(
            @PathVariable Long orderId,
            @RequestParam Long chatroomId) { // DB 저장 X → Controller에서 전달

        PointOrderCompleteDTO response = orderService.completePointOrder(orderId, chatroomId);
        return ResponseEntity.ok(response);
    }

}
