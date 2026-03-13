package com.project.shift.shop.dto;

public class RefundRequestDTO {

    private Long orderId;   // 환불할 주문 ID
    private Integer amount; // 환불 요청 금액 (전체 환불만 허용)

    public Long getOrderId() {
        return orderId;
    }
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Integer getAmount() {
        return amount;
    }
    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
