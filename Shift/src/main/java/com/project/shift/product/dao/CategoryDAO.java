package com.project.shift.product.dao;

import com.project.shift.product.entity.Category;
import com.project.shift.product.repository.CategoryRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * [DAO-002] 카테고리 관련 데이터 접근 클래스
 * ---------------------------------------------------------
 * - PROD-003 : 카테고리 목록 조회
 * - PROD-004 : 카테고리 상세 조회
 */
@Repository
public class CategoryDAO implements ICategoryDAO {

    private final CategoryRepository categoryRepository;

    public CategoryDAO(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /** [PROD-003] 모든 카테고리 목록 조회 */
    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    /** [PROD-004] 카테고리 ID로 카테고리 상세 조회 */
    @Override
    public Category findById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found (ID=" + categoryId + ")"));
    }
}