package com.project.shift.product.dao;

import com.project.shift.product.entity.Product;

/**
 * [DAO-INT-003] 금액권(POINT) DAO 인터페이스
 * ---------------------------------------------------------
 * - category_id = 3 금액권 상품 조회 기능 정의
 */
public interface IPointDAO {

    /** [PROD-011] 금액권 템플릿 상품 조회 */
    Product findPointTemplate();
}