package com.project.shift.product.repository;

import com.project.shift.product.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Review 엔티티를 관리하는 JPA Repository.
 * - 상품 ID 기준으로 리뷰를 조회할 수 있도록 메서드 정의.
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProduct_IdOrderByCreatedDateDesc(Long productId);
}