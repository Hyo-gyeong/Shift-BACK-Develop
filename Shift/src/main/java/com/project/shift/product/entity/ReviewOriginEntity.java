package com.project.shift.product.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.project.shift.product.dto.ReviewOriginDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "REVIEWS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewReviewEntity {

    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "SEQ_REVIEWS"
    )
    @SequenceGenerator(
        name = "SEQ_REVIEWS",
        sequenceName = "SEQ_REVIEWS",
        allocationSize = 1
    )
    @Column(name = "REVIEW_ID")
    private Long reviewId;

    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @Column(name = "PRODUCT_ID", nullable = false)
    private Long productId;

    @Column(name = "RATING", nullable = false)
    private Integer rating;

    @Column(name = "CONTENT", length = 500)
    private String content;

    @Column(name = "CREATED_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdDate;
    
    public static NewReviewEntity toEntity(ReviewOriginDTO dto) {
        return NewReviewEntity.builder()
                .reviewId(dto.getReviewId())
                .userId(dto.getUserId())
                .productId(dto.getProductId())
                .rating(dto.getRating())
                .content(dto.getContent())
                .createdDate(dto.getCreatedDate())
                .build();
    }
}
