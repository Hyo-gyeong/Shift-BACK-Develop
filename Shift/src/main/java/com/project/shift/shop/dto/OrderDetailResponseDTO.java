package com.project.shift.shop.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDetailResponseDTO {

    private Long orderId;
    private Long senderId;
    private Long receiverId;
    private LocalDateTime orderDate;
    private List<OrderDetailItemDTO> items;
    private int totalPrice;
    private PaymentDTO payment;
    private DeliverySimpleDTO delivery;

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }

    public Long getReceiverId() { return receiverId; }
    public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }

    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }

    public List<OrderDetailItemDTO> getItems() { return items; }
    public void setItems(List<OrderDetailItemDTO> items) { this.items = items; }

    public int getTotalPrice() { return totalPrice; }
    public void setTotalPrice(int totalPrice) { this.totalPrice = totalPrice; }

    public PaymentDTO getPayment() { return payment; }
    public void setPayment(PaymentDTO payment) { this.payment = payment; }

    public DeliverySimpleDTO getDelivery() { return delivery; }
    public void setDelivery(DeliverySimpleDTO delivery) {  // ★ 핵심 수정
        this.delivery = delivery;
    }
}
