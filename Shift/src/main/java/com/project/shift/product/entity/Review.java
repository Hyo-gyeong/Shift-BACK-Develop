package com.project.shift.product.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

import com.project.shift.shop.entity.OrderItem;
import com.project.shift.user.entity.UserEntity;

/**
 * reviews 테이블 매핑 엔티티 클래스.
 * - 상품에 대한 사용자의 리뷰 정보를 저장.
 * - 상품 삭제 시 관련 리뷰도 자동 삭제 (ON DELETE CASCADE).
 */
@Entity
@Table(name = "REVIEWS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"product", "user", "orderItem"})
public class Review {

    /** 리뷰 PK */
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_reviews")
    @SequenceGenerator(name = "seq_reviews", sequenceName = "seq_reviews", allocationSize = 1)
    @Column(name = "REVIEW_ID")
    private Long id;

	/** 리뷰 작성자 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserEntity user;

    /** 리뷰가 속한 상품 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private Product product;

    /** 리뷰가 속한 주문상품(order_item) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ITEM_ID", nullable = false)
    private OrderItem orderItem;

    /** 평점 (1~5) */
    @Column(name = "RATING", nullable = false)
    private Integer rating;

    /** 리뷰 내용 */
    @Column(name = "CONTENT", length = 500)
    private String content;

    /** 작성일 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATE", nullable = false)
    private Date createdDate;
}