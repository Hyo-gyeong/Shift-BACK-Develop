package com.project.shift.product.dto;

import java.util.List;

/**
 * 상품 상세 조회 시 응답에 사용될 DTO 클래스.
 */
public class ProductDTO {
    private String productId; // 상품 ID
    private String productName; // 상품명
    private int price; // 가격
    private int stock; // 재고
    private String registrationDate; // 등록 날짜
    private String seller; // 판매자
    private List<String> imageUrls;  // 상품에 대한 이미지 URL 목록

    // 생성자
    public ProductDTO(String productId, String productName, int price, int stock, String registrationDate, String seller, List<String> imageUrls) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.stock = stock;
        this.registrationDate = registrationDate;
        this.seller = seller;
        this.imageUrls = imageUrls;
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

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}