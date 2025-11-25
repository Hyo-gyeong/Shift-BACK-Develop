package com.project.shift.product.dao;

import com.project.shift.product.entity.Product;

/**
 * [DAO-INT-003] 금액권(POINT) DAO 인터페이스
 * ---------------------------------------------------------
 * - category_id = 3 금액권 상품 조회 기능 정의
 * - SHOP-016 : 금액권 주문 생성
 * - SHOP-017 : 금액권 결제 완료 (포인트 적립)
 */
public interface IPointDAO {

    /** [PROD-011] 금액권 템플릿 상품 조회 */
    Product findPointTemplate();

    /** [SHOP-017] 수신자(receiver)의 포인트 적립 */
    int addPoints(Long userId, Integer amount);

    /** [SHOP-017] 포인트 거래내역 기록 (A/U/R) */
    void insertPointTransaction(Long userId, Integer amount, String type);

}