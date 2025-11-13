package com.project.shift.product.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * images 테이블 매핑
 */
@Entity
@Table(name = "IMAGES")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@ToString(exclude = {"product"})
public class Image {

    /** 이미지 ID */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_images")
    @SequenceGenerator(name = "seq_images", sequenceName = "seq_images", allocationSize = 1)
    @Column(name = "IMAGE_ID")
    private Long id;

    /** 상품 ID (외래키) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private Product product;

    /** 이미지 URL */
    @Column(name = "IMAGE_URL", nullable = false, length = 100)
    private String imageUrl;

    /** 대표 이미지 여부 (Y/N) */
    @Column(
        name = "IS_REPRESENTATIVE",
        nullable = false,
        length = 1,
        columnDefinition = "CHAR(1)"
    )
    private String isRepresentative;
}