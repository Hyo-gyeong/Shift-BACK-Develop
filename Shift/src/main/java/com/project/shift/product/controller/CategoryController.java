package com.project.shift.product.controller;

import com.project.shift.product.dto.CategoryDTO;
import com.project.shift.product.dto.ProductDTO;
import com.project.shift.product.service.ICategoryService;
import com.project.shift.product.service.IProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 카테고리 관련 API를 처리하는 컨트롤러입니다.
 * - GET /categories: 모든 카테고리 목록을 조회하는 엔드포인트입니다.
 * - GET /categories/{categoryId}/products: 특정 카테고리에 속한 상품 목록을 조회하는 엔드포인트입니다.
 */
@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final ICategoryService categoryService;
    private final IProductService productService;

    // 의존성 주입: CategoryService와 ProductService 인터페이스를 구현한 구현체가 자동으로 주입됩니다.
    public CategoryController(ICategoryService categoryService, IProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    /**
     * 카테고리 목록을 조회하는 API 메소드입니다.
     * - 클라이언트의 GET /categories 요청을 처리하여 모든 카테고리 정보를 반환합니다.
     * 
     * @return 카테고리 목록 (PROD-003)
     */
    @GetMapping
    public List<CategoryDTO> getCategoryList() {
        // 서비스 레이어에서 카테고리 목록을 조회하여 반환
        return categoryService.getCategoryList();
    }

    /**
     * 특정 카테고리에 속한 상품 목록을 조회하는 API 메소드입니다.
     * - 클라이언트의 GET /categories/{categoryId}/products 요청을 처리하여 해당 카테고리에 속한 상품 목록을 반환합니다.
     * 
     * @param categoryId 카테고리 ID (PROD-004)
     * @return 해당 카테고리에 속한 상품 목록
     */
    @GetMapping("/{categoryId}/products")
    public List<ProductDTO> getProductsByCategory(@PathVariable Long categoryId) {
        // 서비스 레이어에서 카테고리 ID에 해당하는 상품 목록을 조회하여 반환
        return productService.getProductsByCategory(categoryId);
    }
}