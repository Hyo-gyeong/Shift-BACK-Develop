package com.project.shift.shop.service;

import org.springframework.stereotype.Service;

import com.project.shift.shop.dao.IDeliveryDAO;
import com.project.shift.shop.dto.DeliveryDTO;
import com.project.shift.shop.dto.DeliveryStatusUpdateRequestDTO;
import com.project.shift.shop.entity.Delivery;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeliveryService implements IDeliveryService {
	
	private final IDeliveryDAO deliveryDAO;
    
	// SHOP-014 배송 조회
    @Override
    public DeliveryDTO getDeliveryByOrderId(Long orderId) {
        Delivery delivery = deliveryDAO.findByOrderId(orderId);

        if (delivery == null) {
            return null;
        }

        DeliveryDTO dto = new DeliveryDTO();
        dto.setDeliveryId(delivery.getDeliveryId());
        dto.setOrderId(delivery.getOrder().getOrderId());
        dto.setTrackingNumber(delivery.getTrackingNumber());
        dto.setRecipient(delivery.getRecipient());
        dto.setRecipientPhone(delivery.getRecipientPhone());
        dto.setDeliveryAddress(delivery.getDeliveryAddress());
        dto.setDeliveryStatus(delivery.getDeliveryStatus());
        dto.setEstimatedDate(delivery.getEstimatedDate());
        dto.setRequestMessage(delivery.getRequestMessage());

        return dto;
    }
    
	// SHOP-015 배송 상태 변경
    @Override
    public DeliveryDTO updateDeliveryStatus(Long orderId, DeliveryStatusUpdateRequestDTO request) {

        Delivery delivery = deliveryDAO.updateDeliveryStatus(orderId, request.getDeliveryStatus());
        if (delivery == null) {
            return null;
        }

        DeliveryDTO dto = new DeliveryDTO();
        dto.setDeliveryId(delivery.getDeliveryId());
        dto.setOrderId(delivery.getOrder().getOrderId());
        dto.setTrackingNumber(delivery.getTrackingNumber());
        dto.setRecipient(delivery.getRecipient());
        dto.setRecipientPhone(delivery.getRecipientPhone());
        dto.setDeliveryAddress(delivery.getDeliveryAddress());
        dto.setDeliveryStatus(delivery.getDeliveryStatus());
        dto.setEstimatedDate(delivery.getEstimatedDate());
        dto.setRequestMessage(delivery.getRequestMessage());

        return dto;
    }
    
}
