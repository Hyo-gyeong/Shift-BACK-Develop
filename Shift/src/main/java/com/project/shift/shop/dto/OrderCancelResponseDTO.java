package com.project.shift.shop.dto;

public class OrderCancelResponseDTO {

    private Long orderId;
    private Boolean result;

    public OrderCancelResponseDTO() {
    }

    public OrderCancelResponseDTO(Long orderId, Boolean result) {
        this.orderId = orderId;
        this.result = result;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }
}
