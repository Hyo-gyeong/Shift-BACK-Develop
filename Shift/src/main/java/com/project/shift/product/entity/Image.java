package com.project.shift.product.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * images 테이블 매핑
 *
 * 목적:
 * - 목록 카드의 썸네일로 쓸 대표 이미지 1개를 고른다.
 * - is_representative = 'Y' 인 행을 대표 이미지로 간주한다.
 */
@Entity
@Table(name = "images")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@ToString(exclude = "product")
public class Image {

    /** 이미지 PK */
    @Id
    @Column(name = "image_id")
    private Long id;

    /** 상품(FK: product_id). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product; // 이미지가 속하는 상품

    /** 실제 접근 경로(S3 URL, CDN, 내부 저장소 키 등) */
    @Column(name = "image_url", nullable = false, length = 100)
    private String imageUrl;

    /**
     * 대표 이미지 플래그
     * - 값: 'Y' 또는 'N'
     * - 목록 썸네일 선택 기준
     */
    @Column(name = "is_representative", length = 1)
    private String isRepresentative; // 대표 이미지 여부
}
