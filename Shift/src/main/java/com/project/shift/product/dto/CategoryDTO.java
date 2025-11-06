package com.project.shift.product.dto;

import com.project.shift.product.entity.Category;

public class CategoryDTO {

    private Long categoryId;
    private String categoryName;

    public CategoryDTO() {
    }


    public CategoryDTO(Category category) {
        this.categoryId = category.getCategoryId();
        this.categoryName = category.getCategoryName();
    }

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
