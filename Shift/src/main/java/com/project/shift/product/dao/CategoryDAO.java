package com.project.shift.product.dao;

import com.project.shift.product.entity.Category;
import com.project.shift.product.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 카테고리 관련 데이터 접근 객체(DAO) 클래스입니다.
 * - 데이터베이스에서 카테고리 정보를 조회하고, 저장 및 삭제하는 작업을 수행합니다.
 */
@Repository
public class CategoryDAO implements ICategoryDAO {

    private final CategoryRepository categoryRepository; // Repository 의존성 주입

    @Autowired
    public CategoryDAO(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * 모든 카테고리 목록을 조회합니다. (PROD-003)
     */
    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll(); // Repository에서 모든 카테고리 조회
    }

    /**
     * 카테고리 ID로 카테고리 정보를 조회합니다. (PROD-004)
     * 
     * @param categoryId 카테고리 ID
     * @return 카테고리 정보
     */
    @Override
    public Category findById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found")); // ID로 카테고리 조회
    }
}