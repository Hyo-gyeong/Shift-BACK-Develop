package com.project.shift.shop.dto;

import java.time.LocalDate;

/**
 * 배송 상세 조회용 DTO (SHOP-014)
 * 주문별 배송 정보 전체를 프론트로 전달하기 위한 데이터 구조
 */
public class DeliveryDTO {

    private Long deliveryId;
    private Long orderId;
    private String trackingNumber;
    private String recipient;
    private String recipientPhone;
    private String deliveryAddress;
    private String deliveryStatus;   // P/S/D/C
    private LocalDate estimatedDate;
    private String requestMessage;

    public DeliveryDTO() {}

    public Long getDeliveryId() { return deliveryId; }
    public void setDeliveryId(Long deliveryId) { this.deliveryId = deliveryId; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }

    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }

    public String getRecipientPhone() { return recipientPhone; }
    public void setRecipientPhone(String recipientPhone) { this.recipientPhone = recipientPhone; }

    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    public String getDeliveryStatus() { return deliveryStatus; }
    public void setDeliveryStatus(String deliveryStatus) { this.deliveryStatus = deliveryStatus; }

    public LocalDate getEstimatedDate() { return estimatedDate; }
    public void setEstimatedDate(LocalDate estimatedDate) { this.estimatedDate = estimatedDate; }

    public String getRequestMessage() { return requestMessage; }
    public void setRequestMessage(String requestMessage) { this.requestMessage = requestMessage; }
}