package com.project.shift.product.repository;

import com.project.shift.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * [REP-005] 금액권(POINT) Repository
 * ---------------------------------------------------------
 * - PROD-007 : 금액권 대표 이미지 조회
 * - PROD-015 : 금액권 상품 조회
 * ---------------------------------------------------------
 * ※ category_id = 3 인 상품을 금액권 템플릿으로 간주
 */
public interface PointRepository extends JpaRepository<Product, Long> {

    /** [PROD-007] 금액권 대표 이미지 조회 */
    @Query("SELECT i.imageUrl FROM Image i WHERE i.product.category.categoryId = 3 AND i.isRepresentative = 'Y'")
    String findPointImage();

    /** [PROD-015] 금액권 상품 조회 */
    @Query("SELECT p FROM Product p WHERE p.category.categoryId = 3")
    Product findPointProduct();
}