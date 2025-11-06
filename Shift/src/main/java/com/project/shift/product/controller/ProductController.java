package com.project.shift.product.controller;

import com.project.shift.product.dto.ProductDTO;
import com.project.shift.product.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * 전체 상품 목록 조회.
     */
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getProductList() {
        List<ProductDTO> productList = productService.getAllProducts(); // 서비스에서 상품 목록 조회
        return ResponseEntity.ok(productList); // 200 OK로 반환
    }
}
