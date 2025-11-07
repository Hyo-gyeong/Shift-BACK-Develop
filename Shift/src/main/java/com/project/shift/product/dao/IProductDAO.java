package com.project.shift.product.dao;

import com.project.shift.product.entity.Product;

import java.util.List;

/**
 * Product 엔티티와 관련된 데이터베이스 작업을 정의한 DAO 인터페이스.
 */
public interface IProductDAO {

    /**
     * 모든 상품을 조회.	(PROD-001)
     * @return 상품 목록
     */
    List<Product> findAll(); // 상품 목록 조회 메서드 정의
    
    /**
     * 상품을 저장하는 메소드.
     * @param product 저장할 상품 객체
     */
    void saveProduct(Product product); // 상품 저장 메소드 정의
}