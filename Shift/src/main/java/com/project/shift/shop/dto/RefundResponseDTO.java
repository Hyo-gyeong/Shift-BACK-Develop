package com.project.shift.shop.dto;

import java.time.LocalDateTime;

public class RefundResponseDTO {

    private Long orderId;
    private Integer cashRefunded;   // 환불된 현금
    private Integer pointRefunded;  // 환불된 포인트
    private String status;          // "REFUNDED"
    private LocalDateTime refundedAt;
    private Boolean result;         // true / false

    public Long getOrderId() {
        return orderId;
    }
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Integer getCashRefunded() {
        return cashRefunded;
    }
    public void setCashRefunded(Integer cashRefunded) {
        this.cashRefunded = cashRefunded;
    }

    public Integer getPointRefunded() {
        return pointRefunded;
    }
    public void setPointRefunded(Integer pointRefunded) {
        this.pointRefunded = pointRefunded;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getRefundedAt() {
        return refundedAt;
    }
    public void setRefundedAt(LocalDateTime refundedAt) {
        this.refundedAt = refundedAt;
    }

    public Boolean getResult() {
        return result;
    }
    public void setResult(Boolean result) {
        this.result = result;
    }
}
