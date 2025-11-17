package com.project.shift.shop.dto;

public class DeliverySimpleDTO {

    private String deliveryStatus;
    private String trackingNumber;

    public DeliverySimpleDTO() {}

    public DeliverySimpleDTO(String deliveryStatus, String trackingNumber) {
        this.deliveryStatus = deliveryStatus;
        this.trackingNumber = trackingNumber;
    }

    public String getDeliveryStatus() { return deliveryStatus; }
    public void setDeliveryStatus(String deliveryStatus) { this.deliveryStatus = deliveryStatus; }

    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }
}