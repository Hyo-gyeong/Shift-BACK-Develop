package com.project.shift.shop.dto;

public class OrderDetailItemDTO {

    private Long productId;
    private String productName;
    private Integer quantity;
    private Integer itemPrice;
    private Long categoryId;
    private Long orderItemId;


    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Integer getItemPrice() { return itemPrice; }
    public void setItemPrice(Integer itemPrice) { this.itemPrice = itemPrice; }
    
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long long1) { this.categoryId = long1; }
    
	public Long getOrderItemId() { return orderItemId; }
	public void setOrderItemId(Long orderItemId) { this.orderItemId = orderItemId; }

}
