package com.project.shift.shop.dto;

/**
 * 배송 상태 변경 요청 DTO (SHOP-015)
 * 관리자 페이지 없이 Postman 테스트용으로 deliveryStatus 하나만 전달받음.
 */
public class DeliveryStatusUpdateRequestDTO {

    private String deliveryStatus;  // P/S/D/C

    public DeliveryStatusUpdateRequestDTO() {}

    public String getDeliveryStatus() { return deliveryStatus; }
    public void setDeliveryStatus(String deliveryStatus) { this.deliveryStatus = deliveryStatus; }
}