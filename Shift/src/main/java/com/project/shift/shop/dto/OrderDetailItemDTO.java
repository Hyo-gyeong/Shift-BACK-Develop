package com.project.shift.shop.dto;

public class OrderDetailItemDTO {

    private Long productId;
    private String productName;
    private Integer quantity;
    private Integer itemPrice;

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Integer getItemPrice() { return itemPrice; }
    public void setItemPrice(Integer itemPrice) { this.itemPrice = itemPrice; }
}
