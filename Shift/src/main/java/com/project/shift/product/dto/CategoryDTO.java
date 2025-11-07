package com.project.shift.product.dto;

/**
 * 카테고리 정보를 전달하는 데이터 전송 객체(DTO)입니다.
 * - 카테고리 ID와 이름을 포함하여 API 응답에 사용됩니다.
 */
public class CategoryDTO {

    private Long categoryId;    // 카테고리 ID
    private String categoryName; // 카테고리 이름

    // 기본 생성자
    public CategoryDTO() {}

    // 카테고리 ID와 이름을 초기화하는 생성자
    public CategoryDTO(Long categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    // Getters and Setters
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
