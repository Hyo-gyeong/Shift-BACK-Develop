package com.project.shift.product.dao;

import com.project.shift.product.entity.Image;
import java.util.List;

/**
 * [DAO-INT-005] 이미지 DAO 인터페이스
 * ---------------------------------------------------------
 * - ImageDAO의 메서드 시그니처 정의
 */
public interface IImageDAO {

    /** [PROD-007] 특정 상품의 이미지 목록 조회 */
    List<Image> findByProductId(Long productId);
}