package com.project.shift.shop.controller;

import com.project.shift.shop.dto.DeliveryDTO;
import com.project.shift.shop.dto.DeliveryStatusUpdateRequestDTO;
import com.project.shift.shop.service.IDeliveryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/deliveries")
public class DeliveryController {
	
	private final IDeliveryService deliveryService;

    public DeliveryController(IDeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }
	
	// SHOP-014 배송 조회
    @GetMapping("/{orderId}")
    public DeliveryDTO getDelivery(@PathVariable Long orderId) {
        return deliveryService.getDeliveryByOrderId(orderId);
    }
    
	// SHOP-015 배송 상태 변경
    @PutMapping("/{orderId}")
    public DeliveryDTO updateDelivery(
            @PathVariable Long orderId,
            @RequestBody DeliveryStatusUpdateRequestDTO request
    ) {
        return deliveryService.updateDeliveryStatus(orderId, request);
    }
    
}
