package com.project.shift.shop.service;

import com.project.shift.shop.dto.DeliveryDTO;
import com.project.shift.shop.dto.DeliveryStatusUpdateRequestDTO;

public interface IDeliveryService {
	
	// SHOP-014 배송 조회
	DeliveryDTO getDeliveryByOrderId(Long orderId);
	
	// SHOP-015 배송 상태 변경
	DeliveryDTO updateDeliveryStatus(Long orderId, DeliveryStatusUpdateRequestDTO request);
	
}
