package com.project.shift.product.dao;

import com.project.shift.product.entity.Product;
import com.project.shift.product.repository.PointRepository;
import org.springframework.stereotype.Repository;

/**
 * [DAO-003] 금액권(POINT) 관련 데이터 접근 클래스
 * ---------------------------------------------------------
 * - PROD-010 : 대표 이미지 조회
 * - PROD-011 : 금액권 상품 조회
 * ---------------------------------------------------------
 * ※ category_id = 3 인 상품을 금액권 템플릿으로 간주
 */
@Repository
public class PointDAO implements IPointDAO {

    private final PointRepository pointRepository;

    public PointDAO(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    /** [PROD-011] 금액권(category_id=3) 상품 조회 */
    @Override
    public Product findPointTemplate() {
        return pointRepository.findPointProduct();
    }
}