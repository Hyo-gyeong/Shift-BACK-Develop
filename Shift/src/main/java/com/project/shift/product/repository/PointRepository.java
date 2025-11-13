package com.project.shift.product.repository;

import com.project.shift.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * [REP-005] 금액권(POINT) Repository
 * ---------------------------------------------------------
 * - PROD-010 : 금액권 대표 이미지 조회
 * - PROD-011 : 금액권 상품 조회
 * - SHOP-016 : 금액권 주문 생성
 * - SHOP-017 : 금액권 결제 완료 (포인트 적립)
 * ---------------------------------------------------------
 * ※ category_id = 3 인 상품을 금액권 템플릿으로 간주
 */
public interface PointRepository extends JpaRepository<Product, Long> {

    /** [PROD-010] 금액권 대표 이미지 조회 */
    @Query("SELECT i.imageUrl FROM Image i WHERE i.product.category.categoryId = 3 AND i.isRepresentative = 'Y'")
    String findPointImage();

    /** [PROD-011] 금액권 상품 조회 */
    @Query("SELECT p FROM Product p WHERE p.category.categoryId = 3")
    Product findPointProduct();
    
    /** [SHOP-016] 금액권 주문 생성 */
    
    /** [SHOP-017] 금액권 결제 완료 (포인트 적립) */
    
}