package com.project.shift.product.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

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
@ToString(exclude = {"category", "images"}) // 순환 참조 방지
public class Product {

    /** 제품 PK */
    @Id
    @Column(name = "product_id")
    private Long id;

    /**
     * 카테고리 N:1 관계
     * - 컬럼: category_id (NOT NULL)
     * - LAZY: 목록 조회 시 불필요한 조인 회피
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

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

    /**
     * 이미지들: 하나의 상품에는 여러 이미지가 있을 수 있다.
     * - Product에서 여러 이미지를 참조하는 방식으로 설정.
     * - `CascadeType.ALL`로 설정하여 상품 삭제 시 관련 이미지도 함께 삭제
     */
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Image> images; // 상품에 속한 이미지들 (1:N 관계)
    
    // 추가 메서드 (필요시)
    /**
     * 첫 번째 이미지를 반환하는 메서드
     * @return 첫 번째 이미지의 URL
     */
    public String getFirstImageUrl() {
        if (images != null && !images.isEmpty()) {
            return images.get(0).getImageUrl(); // 첫 번째 이미지 URL 반환
        }
        return null; // 이미지가 없으면 null 반환
    }
}
