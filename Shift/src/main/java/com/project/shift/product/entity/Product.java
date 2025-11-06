package com.project.shift.product.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * products 테이블 매핑
 *
 * 설계 메모:
 * 1) 목록 조회 성능을 위해 Category 연관은 LAZY로 둔다.
 * 2) 가격과 재고는 음수 방지 등 비즈니스 검증을 Service 계층에서 처리한다.
 * 3) PK는 DB에 이미 존재한다고 가정(DDL·더미데이터 기준). 신규 등록은 범위 외.
 */
@Entity
@Table(name = "products")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@ToString(exclude = {"images"}) // 이미지 제외
public class Product {

    /** 제품 PK */
    @Id
    @Column(name = "product_id")
    private Long id;

    /** 상품명: 길이 150, NOT NULL */
    @Column(name = "product_name", nullable = false, length = 150)
    private String name;

    /** 가격(원): NOT NULL. 음수 방지는 Service에서 체크 */
    @Column(name = "price", nullable = false)
    private Integer price;

    /** 재고 수량: NOT NULL. 음수 방지는 Service에서 체크 */
    @Column(name = "stock", nullable = false)
    private Integer stock;

    /** 판매자 표기(옵션) */
    @Column(name = "seller", length = 30)
    private String seller;

    // 카테고리와 이미지는 현재 연결하지 않음
    // 추후 필요 시 카테고리 및 이미지 연동 기능을 추가할 예정
}


