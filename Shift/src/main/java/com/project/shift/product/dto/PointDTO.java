package com.project.shift.product.dto;

/**
 * PROD-010 금액권 페이지 정보 응답 DTO
 * - 금액권의 고정 템플릿 정보를 담는다.
 */
public class PointDTO {
    private String productName;  // "포인트 금액권"
    private String imageUrl;     // 대표 이미지
    private String description;  // 금액권 안내 문구
    private int minAmount;       // 최소 입력 금액
    private int maxAmount;       // 최대 입력 금액

    public PointDTO(String productName, String imageUrl, String description, int minAmount, int maxAmount) {
        this.productName = productName;
        this.imageUrl = imageUrl;
        this.description = description;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
    }

    public String getProductName() { 
    	return productName;
    }
    
    public String getImageUrl() { 
    	return imageUrl; 
    }
    
    public String getDescription() { 
    	return description;
    }
    
    public int getMinAmount() { 
    	return minAmount; 
    }
    
    public int getMaxAmount() { 
    	return maxAmount;
    }
}