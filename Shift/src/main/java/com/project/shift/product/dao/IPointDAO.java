package com.project.shift.product.dao;

import com.project.shift.product.entity.Product;

/**
 * 금액권 관련 DAO 인터페이스
 * --------------------------------------------
 * - 금액권(category_id=3) 상품 정보 조회
 */
public interface IPointDAO {
    Product findPointTemplate();
}