package com.project.shift.product.controller;

import com.project.shift.product.dto.ImageDTO;
import com.project.shift.product.dto.ProductDTO;
import com.project.shift.product.entity.Product;
import com.project.shift.product.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * [CTRL-003] 상품 관련 API 컨트롤러
 * ---------------------------------------------------------
 * - PROD-001 : 전체 상품 목록 조회
 * - PROD-002 : 상품 상세 조회
 * - PROD-004 : 카테고리별 상품 조회
 * - PROD-005 : 상품 검색
 * - PROD-006 : 상품 정렬
 * - PROD-007 : 상품 이미지 조회
 * - PROD-009 : 상품 재고 조회
 * ---------------------------------------------------------
 * ※ 금액권(Category_ID = 3)은 일반 상품 조회 대상에서 제외됨
 */
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /** [PROD-001] 전체 상품 목록 조회 */
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getProductList() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    /** [PROD-002] 상품 상세 조회 */
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> getProductDetails(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProductDetails(productId));
    }

    /** 상품 등록 (시퀀스 기반 자동 ID 생성) */
    @PostMapping
    public ResponseEntity<String> addProduct(@RequestBody Product product) {
        productService.saveProduct(product);
        return ResponseEntity.ok("Product Added Successfully");
    }

    /** [PROD-005] 상품명 부분 일치 검색 */
    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchProducts(@RequestParam String keyword) {
        return ResponseEntity.ok(productService.searchProducts(keyword));
    }

    /**
     * [PROD-006] 상품 정렬 목록 조회
     * 허용값: priceAsc | priceDesc | latest
     * null 또는 공백은 latest로 처리
     */
    @GetMapping("/sort")
    public ResponseEntity<?> getSortedProducts(@RequestParam(name = "sortType", required = false) String sortType) {
        if (sortType != null && !List.of("priceAsc", "priceDesc", "latest").contains(sortType)) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Invalid sortType",
                    "allowed", List.of("priceAsc", "priceDesc", "latest"),
                    "received", sortType
            ));
        }
        return ResponseEntity.ok(productService.getSortedProducts(sortType));
    }

    /** [PROD-004 + PROD-006] 카테고리별 정렬 조회 */
    @GetMapping("/categories/{categoryId}/products/sort")
    public ResponseEntity<?> getCategorySorted(
            @PathVariable Long categoryId,
            @RequestParam(name = "sortType", required = false) String sortType) {

        if (sortType != null && !List.of("priceAsc", "priceDesc", "latest").contains(sortType)) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Invalid sortType",
                    "allowed", List.of("priceAsc", "priceDesc", "latest"),
                    "received", sortType
            ));
        }
        return ResponseEntity.ok(productService.getProductsByCategorySorted(categoryId, sortType));
    }

    /** [PROD-007] 상품 이미지 목록 조회 (대표/상세 이미지 포함) */
    @GetMapping("/{productId}/images")
    public ResponseEntity<List<ImageDTO>> getProductImages(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProductImages(productId));
    }

    /** [PROD-009] 상품 재고 조회 */
    @GetMapping("/{productId}/stock")
    public ResponseEntity<ProductDTO> getProductStock(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProductStock(productId));
    }
}