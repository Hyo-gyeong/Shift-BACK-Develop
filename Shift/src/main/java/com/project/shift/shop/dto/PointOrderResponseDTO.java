package com.project.shift.shop.dto;

/**
 * [DTO] SHOP-016 금액권 주문 생성 응답 DTO
 */
public class PointOrderResponseDTO {

    private Long orderId;
    private Long senderId;
    private Long receiverId;
    private Long chatroomId;  // DB에는 저장 안함 / 응답용
    private Integer amount;
    private String status;
    private Boolean result;

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }

    public Long getReceiverId() { return receiverId; }
    public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }

    public Long getChatroomId() { return chatroomId; }
    public void setChatroomId(Long chatroomId) { this.chatroomId = chatroomId; }

    public Integer getAmount() { return amount; }
    public void setAmount(Integer amount) { this.amount = amount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Boolean getResult() { return result; }
    public void setResult(Boolean result) { this.result = result; }
}