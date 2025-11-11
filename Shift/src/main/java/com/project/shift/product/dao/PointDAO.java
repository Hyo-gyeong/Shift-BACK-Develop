package com.project.shift.product.dao;

import com.project.shift.product.entity.Product;
import com.project.shift.product.repository.PointRepository;
import org.springframework.stereotype.Service;

/**
 * 금액권 DAO 구현 클래스
 * --------------------------------------------
 * - category_id=3 금액권 상품 및 이미지 데이터 접근
 * - PROD-010 : 대표 이미지 조회 (Repository 직접 호출)
 * - PROD-011 : 금액권 상품 조회 (PRICE/STOCK 조회용)
 */
@Service
public class PointDAO implements IPointDAO {

    private final PointRepository pointRepository;

    public PointDAO(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    /**
     * [PROD-011]
     * 금액권(category_id=3) 상품 조회
     * - 금액권의 최소/최대 금액(PRICE/STOCK)을 사용하기 위함
     */
    @Override
    public Product findPointTemplate() {
        return pointRepository.findPointProduct();
    }
}