package com.project.shift.shop.dto;

public class PaymentDTO {

    private Integer cashAmount;
    private Integer pointUsed;

    public PaymentDTO() {}

    public PaymentDTO(Integer cashAmount, Integer pointUsed) {
        this.cashAmount = cashAmount;
        this.pointUsed = pointUsed;
    }

    public Integer getCashAmount() { return cashAmount; }
    public void setCashAmount(Integer cashAmount) { this.cashAmount = cashAmount; }

    public Integer getPointUsed() { return pointUsed; }
    public void setPointUsed(Integer pointUsed) { this.pointUsed = pointUsed; }
}
