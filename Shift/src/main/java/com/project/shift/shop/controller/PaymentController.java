package com.project.shift.shop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.shift.global.jwt.JwtService;
import com.project.shift.shop.dto.PaymentRequestDTO;
import com.project.shift.shop.dto.PaymentResponseDTO;
import com.project.shift.shop.dto.PaymentResultDTO;
import com.project.shift.shop.service.IOrderService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

	private final IOrderService orderService;
	private final JwtService jwtService;
	
    // SHOP-009 결제 요청
    @PostMapping
    public ResponseEntity<PaymentResponseDTO> requestPayment(@RequestBody PaymentRequestDTO request) {
        PaymentResponseDTO response = orderService.requestPayment(request);
        return ResponseEntity.ok(response);
    }
    
    // SHOP-010 결제 결과 조회
    @GetMapping("/{orderId}")
    public ResponseEntity<PaymentResultDTO> getPaymentResult(@PathVariable Long orderId) {
        PaymentResultDTO response = orderService.getPaymentResult(orderId);
        return ResponseEntity.ok(response);
    }
    
	// SHOP-018 선물 결제 및 채팅이 전송
    @PostMapping("/gift/{chatroomId}")
    public ResponseEntity<PaymentResponseDTO> requestGiftPayment(HttpServletRequest request, @RequestBody PaymentRequestDTO dto,
    																@PathVariable long chatroomId) {
    	// jwt에서 현재 사용자의 토큰 추출
		String token = jwtService.extractTokenFromRequest(request);
        // 토큰에서 현재 사용자의 PK 추출
		long userId = jwtService.extractUserIdFromValidToken(token);
    		
		PaymentResponseDTO response = orderService.requestGiftPayment(dto, chatroomId, userId);
        return ResponseEntity.ok(response);
    }
}
