package com.project.shift.shop.dto;

public class DeliveryDTO {

    private String status;
    private String trackingNumber;

    public DeliveryDTO() {}

    public DeliveryDTO(String status, String trackingNumber) {
        this.status = status;
        this.trackingNumber = trackingNumber;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }
}
