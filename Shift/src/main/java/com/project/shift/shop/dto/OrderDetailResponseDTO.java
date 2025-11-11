package com.project.shift.shop.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDetailResponseDTO {

    private Long orderId;
    private Long senderId;
    private Long receiverId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime orderDate;

    private List<OrderDetailItemDTO> items;
    private Integer totalPrice;
    private PaymentDTO payment;
    private DeliveryDTO delivery;

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

    public Integer getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Integer totalPrice) { this.totalPrice = totalPrice; }

    public PaymentDTO getPayment() { return payment; }
    public void setPayment(PaymentDTO payment) { this.payment = payment; }

    public DeliveryDTO getDelivery() { return delivery; }
    public void setDelivery(DeliveryDTO delivery) { this.delivery = delivery; }
}
