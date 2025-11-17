package com.project.shift.shop.dto;

/**
 * [DTO] SHOP-017 금액권 결제 완료 응답 DTO
 */
public class PointOrderCompleteDTO {

    private Long orderId;
    private Long chatroomId;  // Controller에서 전달
    private Long receiverId;
    private Integer addedPoints;
    private Integer updatedTotalPoints;
    private Boolean result;

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public Long getChatroomId() { return chatroomId; }
    public void setChatroomId(Long chatroomId) { this.chatroomId = chatroomId; }

    public Long getReceiverId() { return receiverId; }
    public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }

    public Integer getAddedPoints() { return addedPoints; }
    public void setAddedPoints(Integer addedPoints) { this.addedPoints = addedPoints; }

    public Integer getUpdatedTotalPoints() { return updatedTotalPoints; }
    public void setUpdatedTotalPoints(Integer updatedTotalPoints) { this.updatedTotalPoints = updatedTotalPoints; }

    public Boolean getResult() { return result; }
    public void setResult(Boolean result) { this.result = result; }
}