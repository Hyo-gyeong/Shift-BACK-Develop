package com.project.shift.shop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.shift.shop.dto.RefundRequestDTO;
import com.project.shift.shop.dto.RefundResponseDTO;
import com.project.shift.shop.service.IOrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/refunds")
@RequiredArgsConstructor
public class RefundController {

	private final IOrderService orderService;
	
    // SHOP-013 환불 요청
    @PostMapping
    public ResponseEntity<RefundResponseDTO> requestRefund(@RequestBody RefundRequestDTO requestDTO) {
        RefundResponseDTO response = orderService.requestRefund(requestDTO);
        return ResponseEntity.ok(response);
    }
    
}
