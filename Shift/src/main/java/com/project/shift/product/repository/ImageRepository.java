package com.project.shift.product.repository;

import com.project.shift.product.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * [REP-003] 이미지 Repository
 * ---------------------------------------------------------
 * - PROD-007 : 상품 이미지 조회
 * ---------------------------------------------------------
 * ※ Product의 PK 필드는 'id' 이므로 'findByProduct_Id' 사용
 */
public interface ImageRepository extends JpaRepository<Image, Long> {

    /** [PROD-007] 상품 ID 기준 이미지 조회 */
    List<Image> findByProduct_Id(Long productId);
}