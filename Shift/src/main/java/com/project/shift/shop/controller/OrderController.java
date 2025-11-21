package com.project.shift.shop.controller;

import static com.project.shift.global.security.CurrentUser.getUserIdOrNull;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.shift.global.jwt.JwtService;
import com.project.shift.shop.dto.OrderCancelResponseDTO;
import com.project.shift.shop.dto.OrderDTO;
import com.project.shift.shop.dto.OrderDetailResponseDTO;
import com.project.shift.shop.dto.OrderListResponseDTO;
import com.project.shift.shop.dto.PaymentRequestDTO;
import com.project.shift.shop.dto.PaymentResponseDTO;
import com.project.shift.shop.dto.PaymentResultDTO;
import com.project.shift.shop.dto.PointOrderCompleteDTO;
import com.project.shift.shop.dto.PointOrderRequestDTO;
import com.project.shift.shop.dto.PointOrderResponseDTO;
import com.project.shift.shop.dto.RefundRequestDTO;
import com.project.shift.shop.dto.RefundResponseDTO;
import com.project.shift.shop.service.IOrderService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping
@RequiredArgsConstructor // 생성자 주입을 임의의 코드없이 자동으로 설정해주는 어노테이션
public class OrderController {

    private final IOrderService orderService;
    private final JwtService jwtService;

    // SHOP-006 주문 생성
    @PostMapping("/orders")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO request) {
    	  Long uid = getUserIdOrNull();
          if (uid != null) request.setSenderId(uid); 
          return ResponseEntity.ok(orderService.createOrder(request));
    }
    
    //SHOP-007 주문 내역 조회
    @GetMapping("/orders")
    public ResponseEntity<OrderListResponseDTO> getOrders(@RequestParam(value="userId", required=false) Long userId) {
        Long uid = getUserIdOrNull();
        Long effectiveUserId = uid != null ? uid : userId;
        if (effectiveUserId == null) throw new IllegalArgumentException("userId가 필요합니다. (JWT 또는 파라미터)");
        return ResponseEntity.ok(orderService.getOrdersByUser(effectiveUserId));
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
    
    // ##### 선물하기 버튼을 눌렀을 때 결제 및 채팅이 전송되는 API #####
    @PostMapping("/payments/gift/{chatroomId}")
    public ResponseEntity<PaymentResponseDTO> requestGiftPayment(HttpServletRequest request, @RequestBody PaymentRequestDTO dto,
    																@PathVariable long chatroomId) {
    	// jwt에서 현재 사용자의 토큰 추출
		String token = jwtService.extractTokenFromRequest(request);
        // 토큰에서 현재 사용자의 PK 추출
		long userId = jwtService.extractUserIdFromValidToken(token);
    		
		PaymentResponseDTO response = orderService.requestGiftPayment(dto, chatroomId, userId);
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
    
    // SHOP-013 환불 요청
    @PostMapping("/refunds")
    public ResponseEntity<RefundResponseDTO> requestRefund(@RequestBody RefundRequestDTO requestDTO) {
        RefundResponseDTO response = orderService.requestRefund(requestDTO);
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
