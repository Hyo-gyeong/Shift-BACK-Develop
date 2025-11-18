package com.project.shift.shop.dto;

/**
 * [DTO] SHOP-016 금액권 주문 생성 요청 DTO
 * ---------------------------------------------------------
 * - senderId : 주문 요청자
 * - chatroomId : receiver 계산용
 * - categoryId : 금액권 카테고리 (항상 3)
 * - amount : 금액권 가격
 */
public class PointOrderRequestDTO {

    private Long senderId;
    private Long chatroomId;  // DB 저장 X, receiver 계산용
    private Long categoryId;  // 항상 3
    private Integer amount;

    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }

    public Long getChatroomId() { return chatroomId; }
    public void setChatroomId(Long chatroomId) { this.chatroomId = chatroomId; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public Integer getAmount() { return amount; }
    public void setAmount(Integer amount) { this.amount = amount; }
}