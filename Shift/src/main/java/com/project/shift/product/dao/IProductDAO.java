package com.project.shift.product.dao;

import com.project.shift.product.entity.Product;

import java.util.List;

/**
 * Product 엔티티와 관련된 데이터베이스 작업을 정의한 DAO 인터페이스.
 */
public interface IProductDAO {
    List<Product> findAll(); // 모든 상품 조회 메서드 정의
}