package com.project.shift.product.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * images 테이블 매핑
 */
@Entity
@Table(name = "IMAGES")  // 테이블 이름을 대문자로 지정
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@ToString(exclude = {"product"})  // 상품과 이미지를 제외한 toString() 출력
public class Image {

    /** 이미지 ID */
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_images_generator")
    @SequenceGenerator(name = "seq_images_generator", sequenceName = "seq_images", allocationSize = 1)
    @Column(name = "IMAGE_ID")
    private Long id;

    /** 상품 ID (외래키) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private Product product;

    /** 이미지 URL */
    @Column(name = "IMAGE_URL", nullable = false)
    private String imageUrl;

    /** 대표 이미지 여부 */
    @Column(name = "IS_REPRESENTATIVE", nullable = false)
    private String isRepresentative;  // Y: 대표 이미지, N: 기타 이미지

    /**
     * 이미지 URL을 반환하는 메소드 추가
     */
    public String getImageUrl() {
        return this.imageUrl;
    }
}