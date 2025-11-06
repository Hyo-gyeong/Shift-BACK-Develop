package com.project.shift.product.controller;

import com.project.shift.product.dto.ProductDTO;
import com.project.shift.product.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 상품 관련 API.
 */
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * 전체 상품 목록 조회(카테고리 필터)
     */
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getProductList(
            @RequestParam(value = "categoryId", required = false) Long categoryId
    ) {
        List<ProductDTO> productList;

        if (categoryId != null) {
            productList = productService.getProductsByCategory(categoryId);
        } else {
            productList = productService.getAllProducts();
        }

        return ResponseEntity.ok(productList);
    }
}

