package com.project.shift.product.controller;

import com.project.shift.product.dto.CategoryDTO;
import com.project.shift.product.dto.ProductDTO;
import com.project.shift.product.service.ICategoryService;
import com.project.shift.product.service.IProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * [CTRL-001] 카테고리 관련 API 컨트롤러
 * ---------------------------------------------------------
 * - PROD-003 : 카테고리 목록 조회
 * - PROD-004 : 카테고리별 상품 목록 조회
 */
@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final ICategoryService categoryService;
    private final IProductService productService;

    public CategoryController(ICategoryService categoryService, IProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    /** [PROD-003] 전체 카테고리 목록 조회 */
    @GetMapping
    public List<CategoryDTO> getCategoryList() {
        return categoryService.getCategoryList();
    }

    /** [PROD-004] 특정 카테고리에 속한 상품 목록 조회 */
    @GetMapping("/{categoryId}/products")
    public List<ProductDTO> getProductsByCategory(@PathVariable Long categoryId) {
        return productService.getProductsByCategory(categoryId);
    }
}