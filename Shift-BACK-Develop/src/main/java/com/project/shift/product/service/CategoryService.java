package com.project.shift.product.service;

import com.project.shift.product.dao.CategoryDAO;
import com.project.shift.product.dto.CategoryDTO;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

/**
 * [SERVICE-004] 카테고리 관련 비즈니스 로직 처리 클래스
 * ---------------------------------------------------------
 * - PROD-003 : 카테고리 목록 조회
 */
@Service
public class CategoryService implements ICategoryService {

    private final CategoryDAO categoryDAO;

    public CategoryService(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    /** [PROD-003] 카테고리 목록 조회 */
    @Override
    public List<CategoryDTO> getCategoryList() {
        return categoryDAO.findAll().stream()
                .map(c -> new CategoryDTO(c.getCategoryId(), c.getCategoryName()))
                .collect(Collectors.toList());
    }
}