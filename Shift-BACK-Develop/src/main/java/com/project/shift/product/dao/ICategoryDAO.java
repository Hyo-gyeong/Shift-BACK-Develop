package com.project.shift.product.dao;

import com.project.shift.product.entity.Category;
import java.util.List;

/**
 * [DAO-INT-002] 카테고리 DAO 인터페이스
 * ---------------------------------------------------------
 * - CategoryDAO의 메서드 시그니처 정의
 */
public interface ICategoryDAO {

    /** [PROD-003] 전체 카테고리 목록 조회 */
    List<Category> findAll();

    /** [PROD-004] 카테고리 상세 조회 */
    Category findById(Long categoryId);
}