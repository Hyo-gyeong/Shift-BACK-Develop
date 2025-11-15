package com.project.shift.shop.dao;

import com.project.shift.shop.entity.Delivery;
import com.project.shift.shop.repository.DeliveryRepository;
import org.springframework.stereotype.Repository;

@Repository
public class DeliveryDAO implements IDeliveryDAO {
	
	private final DeliveryRepository deliveryRepository;

    public DeliveryDAO(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }
    
	// SHOP-014 배송 조회
    @Override
    public Delivery findByOrderId(Long orderId) {
        return deliveryRepository
                .findByOrder_OrderId(orderId)
                .orElse(null);
    }
    
	// SHOP-015 배송 상태 변경
    @Override
    public Delivery updateDeliveryStatus(Long orderId, String newStatus) {

        Delivery delivery = deliveryRepository
                .findByOrder_OrderId(orderId)
                .orElse(null);

        if (delivery == null) return null;

        delivery.setDeliveryStatus(newStatus);
        return deliveryRepository.save(delivery);
    }
    
}
