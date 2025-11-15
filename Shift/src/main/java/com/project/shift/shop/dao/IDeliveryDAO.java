package com.project.shift.shop.dao;

import com.project.shift.shop.entity.Delivery;

public interface IDeliveryDAO {
	
	// SHOP-014 배송 조회
	Delivery findByOrderId(Long orderId);
	
	// SHOP-015 배송 상태 변경
	Delivery updateDeliveryStatus(Long orderId, String newStatus);
	
}
