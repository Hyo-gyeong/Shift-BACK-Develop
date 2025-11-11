package com.project.shift.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.project.shift.product.entity.Image;
import com.project.shift.product.entity.Product;

/**
 * 금액권(category_id=3) 관련 Repository
 * ----------------------------------------
 * - PROD-010 : 대표 이미지 조회
 * - PROD-011 : 금액권 상품 정보 조회 (최소/최대 금액)
 */
@Repository
public interface PointRepository extends JpaRepository<Image, Long> {

    /**
     * [PROD-010]
     * category_id=3 금액권의 대표 이미지 URL 조회
     */
    @Query("SELECT i.imageUrl FROM Image i WHERE i.product.category.categoryId = 3 AND i.isRepresentative = 'Y'")
    String findPointImage();

    /**
     * [PROD-011]
     * category_id=3 금액권 상품 조회 (PRICE=최소금액, STOCK=최대금액)
     */
    @Query("SELECT p FROM Product p WHERE p.category.categoryId = 3")
    Product findPointProduct();
}