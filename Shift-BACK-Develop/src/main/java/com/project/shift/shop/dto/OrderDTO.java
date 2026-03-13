package com.project.shift.shop.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO {

    private Long orderId;
    private Long senderId;
    private Long receiverId;
    private Long chatroomId;
    private Integer totalPrice;
    private LocalDateTime orderDate;
    private String orderStatus;
    private Boolean result;

    private List<OrderItemDTO> items;



    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }
    public Long getReceiverId() { return receiverId; }
    public Long getChatroomId() { return chatroomId; }
    public void setChatroomId(Long chatroomId) { this.chatroomId = chatroomId; }
    public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }
    public Integer getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Integer totalPrice) { this.totalPrice = totalPrice; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    public String getOrderStatus() { return orderStatus; }
    public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }
    public Boolean getResult() { return result; }
    public void setResult(Boolean result) { this.result = result; }
    public List<OrderItemDTO> getItems() { return items; }
    public void setItems(List<OrderItemDTO> items) { this.items = items; }
}
