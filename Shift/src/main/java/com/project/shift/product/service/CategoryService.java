package com.project.shift.product.service;

import com.project.shift.product.dto.CategoryDTO;
import com.project.shift.product.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 카테고리 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 * - 카테고리 목록을 조회하는 로직을 포함하고 있습니다.
 */
@Service
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;

    // CategoryRepository를 의존성 주입을 통해 초기화
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * 카테고리 목록을 조회하는 메소드입니다.
     * - 데이터베이스에서 모든 카테고리 정보를 조회하고, 이를 DTO로 변환하여 반환합니다.
     * 
     * @return 카테고리 목록
     */
    @Override
    public List<CategoryDTO> getCategoryList() {
        // 카테고리 목록을 조회하여 DTO 리스트로 변환
        return categoryRepository.findAll().stream()
                .map(category -> new CategoryDTO(category.getCategoryId(), category.getCategoryName()))
                .collect(Collectors.toList()); // 최종적으로 List<CategoryDTO> 반환
    }
}
