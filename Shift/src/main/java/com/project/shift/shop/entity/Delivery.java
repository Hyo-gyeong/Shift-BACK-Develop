package com.project.shift.shop.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "DELIVERIES")
@SequenceGenerator(
        name = "SEQ_DELIVERIES_GEN",
        sequenceName = "SEQ_DELIVERIES",
        allocationSize = 1
)
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_DELIVERIES_GEN")
    @Column(name = "DELIVERY_ID")
    private Long deliveryId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID", nullable = false)
    private Order order;

    @Column(name = "TRACKING_NUMBER", unique = true)
    private String trackingNumber;

    @Column(name = "RECIPIENT", nullable = false)
    private String recipient;

    @Column(name = "RECIPIENT_PHONE", nullable = false)
    private String recipientPhone;

    @Column(name = "DELIVERY_ADDRESS", nullable = false)
    private String deliveryAddress;

    @Column(name = "DELIVERY_STATUS")
    private String deliveryStatus;

    @Column(name = "ESTIMATED_DATE")
    private LocalDate estimatedDate;

    @Column(name = "REQUEST_MESSAGE")
    private String requestMessage;

    // getter/setter
    public Long getDeliveryId() { return deliveryId; }
    public void setDeliveryId(Long deliveryId) { this.deliveryId = deliveryId; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }

    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }

    public String getRecipientPhone() { return recipientPhone; }
    public void setRecipientPhone(String recipientPhone) { this.recipientPhone = recipientPhone; }

    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    public String getDeliveryStatus() { return deliveryStatus; }
    public void setDeliveryStatus(String deliveryStatus) { this.deliveryStatus = deliveryStatus; }

    public LocalDate getEstimatedDate() { return estimatedDate; }
    public void setEstimatedDate(LocalDate estimatedDate) { this.estimatedDate = estimatedDate; }

    public String getRequestMessage() { return requestMessage; }
    public void setRequestMessage(String requestMessage) { this.requestMessage = requestMessage; }
}
