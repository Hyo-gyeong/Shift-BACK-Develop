package com.project.shift.shop.dto;
import java.time.LocalDateTime;

public class PaymentDTO {

    private Integer cashAmount;
    private Integer pointUsed;
    private String status;              // ★ SUCCESS/PENDING/CANCELED
    private LocalDateTime approvedAt;
    public PaymentDTO() {}

    public PaymentDTO(Integer cashAmount, Integer pointUsed) {
        this.cashAmount = cashAmount;
        this.pointUsed = pointUsed;
    }

    public Integer getCashAmount() { return cashAmount; }
    public void setCashAmount(Integer cashAmount) { this.cashAmount = cashAmount; }

    public Integer getPointUsed() { return pointUsed; }
    public void setPointUsed(Integer pointUsed) { this.pointUsed = pointUsed; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getApprovedAt() { return approvedAt; }
    public void setApprovedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; }
}
