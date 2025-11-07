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

    private final CategoryRepository categoryRepository;

    // 의존성 주입: CategoryRepository를 통해 JPA를 사용하여 데이터베이스와 상호작용
    @Autowired
    public CategoryDAO(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * 모든 카테고리 목록을 조회합니다.	(PROD-003)
     * 
     * @return 카테고리 목록
     */
    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    /**
     * 카테고리 ID로 카테고리 정보를 조회합니다.	(PROD-004)
     * 
     * @param categoryId 카테고리 ID
     * @return 카테고리 정보
     */
    @Override
    public Category findById(Long categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        return category.orElse(null); // 카테고리가 존재하지 않으면 null 반환
    }

}