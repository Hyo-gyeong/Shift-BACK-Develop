package com.project.shift.product.entity;

import jakarta.persistence.*;

/**
 * 카테고리 엔티티 클래스입니다.
 * - 데이터베이스의 'CATEGORIES' 테이블과 매핑됩니다.
 * - 카테고리 ID와 카테고리 이름을 관리합니다.
 */
@Entity
@Table(name = "CATEGORIES")  // 테이블 이름을 대소문자 맞추기
public class Category {

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_categories")
    @SequenceGenerator(name = "seq_categories", sequenceName = "seq_categories", allocationSize = 1)
    @Column(name = "CATEGORY_ID")
    private Long categoryId;

	@Column(name = "CATEGORY_NAME", length = 50, nullable = false)
    private String categoryName;

    // 기본 생성자
    public Category() {}

    // 카테고리 ID와 이름을 초기화하는 생성자
    public Category(Long categoryId, String categoryName) {
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