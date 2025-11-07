package com.project.shift.product.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

/**
 * products 테이블 매핑
 */
@Entity
@Table(name = "PRODUCTS")  // 테이블 이름을 대문자로 지정
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@ToString(exclude = {"category", "images"}) // 카테고리와 이미지 제외
public class Product {

    /** 제품 PK */
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_products_generator")
    @SequenceGenerator(name = "seq_products_generator", sequenceName = "seq_products", allocationSize = 1)
    @Column(name = "PRODUCT_ID")
    private Long id;

    /** 상품명: 길이 150, NOT NULL */
    @Column(name = "PRODUCT_NAME", nullable = false, length = 150)
    private String name;

    /** 가격(원): NOT NULL. */
    @Column(name = "PRICE", nullable = false)
    private Integer price;

    /** 재고 수량: NOT NULL. */
    @Column(name = "STOCK", nullable = false)
    private Integer stock;

    /** 판매자 표기(옵션) */
    @Column(name = "SELLER", length = 30)
    private String seller;

    /** 상품 등록일 (등록 시 자동으로 SYSDATE로 설정) */
    @Column(name = "REGISTRATION_DATE", nullable = false, updatable = false)
    private Date registrationDate;

    /**
     * 이미지들: 하나의 상품에는 여러 이미지가 있을 수 있다.
     * - Product에서 여러 이미지를 참조하는 방식으로 설정.
     * - `CascadeType.ALL`로 설정하여 상품 삭제 시 관련 이미지도 함께 삭제
     */
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Image> images; // 상품에 속한 이미지들 (1:N 관계)
    
    /** 카테고리 (상품은 하나의 카테고리에 속함) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID", nullable = false)
    private Category category; // 상품에 속한 카테고리

    // 상품 등록일을 반환하는 getter 메소드 추가
    public Date getRegistrationDate() {
        return this.registrationDate;
    }
}