package com.project.shift.product.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "IMAGES")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;  // 이미지 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;  // 상품과의 관계 (ManyToOne)

    @Column(name = "image_url", nullable = false)
    private String imageUrl;  // 이미지 URL

    @Column(name = "is_representative", nullable = false)
    private String isRepresentative;  // 대표 이미지 여부 (Y / N)

    // Getter, Setter (Lombok @Getter, @Setter 사용 가능)
}