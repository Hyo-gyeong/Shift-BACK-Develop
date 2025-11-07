package com.project.shift.product.dto;

/**
 * 상품 목록 조회 시 응답에 사용될 DTO 클래스.
 */
public class ProductDTO {
    private String productId; // 상품 ID
    private String productName; // 상품명
    private int price; // 가격
    private int stock; // 재고

    // 생성자
    public ProductDTO(String productId, String productName, int price, int stock) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.stock = stock;
    }

    // Getter 및 Setter
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}