package com.project.shift.product.controller;

import com.project.shift.product.dto.ImageDTO;
import com.project.shift.product.dto.ProductDTO;
import com.project.shift.product.entity.Product;
import com.project.shift.product.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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
     * 전체 상품 목록 조회. (PROD-001)
     */
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getProductList() {
        List<ProductDTO> productList = productService.getAllProducts(); // 서비스에서 상품 목록 조회
        return ResponseEntity.ok(productList); // 200 OK로 반환
    }
    
    /**
     * 상품 상세 조회 (PROD-002)
     */
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> getProductDetails(@PathVariable Long productId) {
        ProductDTO productDTO = productService.getProductDetails(productId); // 상품 상세 조회 서비스 호출
        return ResponseEntity.ok(productDTO); // 200 OK로 반환
    }
    
    /**
     * 상품 추가 (시퀀스를 사용하여 상품 저장)
     */
    @PostMapping
    public ResponseEntity<String> addProduct(@RequestBody Product product) {
        productService.saveProduct(product); // 상품 저장
        return ResponseEntity.ok("Product Added Successfully");
    }
    
    /**
     * 상품명 부분일치 검색 (PROD-005)
     * @param keyword 검색어
     * @return 검색된 상품 목록
     */
    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchProducts(@RequestParam String keyword) {
        List<ProductDTO> productList = productService.searchProducts(keyword); // 서비스에서 상품 검색
        return ResponseEntity.ok(productList); // 200 OK로 반환
    }
    
    /**
     * 상품 정렬 목록 조회. (PROD-006)
     * 허용값: priceAsc | priceDesc | latest
     * null/빈값은 latest 처리.
     *
     * 예)
     *  - /products/sort?sortType=priceAsc
     *  - /products/sort?sortType=priceDesc
     *  - /products/sort            // 기본 latest
     *
     * 정책:
     *  - 엄격 검증 활성화: 허용 외 값은 400 Bad Request.
     */
    @GetMapping("/sort")
    public ResponseEntity<?> getSortedProducts(
            @RequestParam(name = "sortType", required = false) String sortType) {

        if (sortType != null) {
            switch (sortType) {
                case "priceAsc":
                case "priceDesc":
                case "latest":
                    break;
                default:
                    return ResponseEntity.badRequest().body(
                        Map.of(
                            "error", "Invalid sortType",
                            "allowed", List.of("priceAsc","priceDesc","latest"),
                            "received", sortType
                        )
                    );
            }
        }

        List<ProductDTO> items = productService.getSortedProducts(sortType);
        return ResponseEntity.ok(items);
    }
    
    /**
     * 카테고리 한정 정렬		(PROD-004 + PROD-006)
     * @param categoryId
     * @param sortType
     * @return
     */
    @GetMapping("/categories/{categoryId}/products/sort")
    public ResponseEntity<?> getCategorySorted(
            @PathVariable Long categoryId,
            @RequestParam(name = "sortType", required = false) String sortType) {

        if (sortType != null) {
            switch (sortType) {
                case "priceAsc": case "priceDesc": case "latest": break;
                default: return ResponseEntity.badRequest().body(
                    Map.of("error","Invalid sortType",
                           "allowed", List.of("priceAsc","priceDesc","latest"),
                           "received", sortType));
            }
        }
        return ResponseEntity.ok(productService.getProductsByCategorySorted(categoryId, sortType));
    }
    
    /**
     *	상품 이미지 조회 	(PROD-007)
     * ----------------------------------------------------------
     * 특정 상품의 이미지 목록(대표/상세 이미지 포함)을 조회한다.
     * - 상품 목록 카드에서는 대표 이미지(Y)만 사용
     * - 상품 상세 페이지에서는 모든 이미지(Y/N)를 노출

     * @param productId 상품 ID (PathVariable)
     * @return 해당 상품의 이미지 목록 (대표 + 상세)
     */
    @GetMapping("/{productId}/images")
    public ResponseEntity<List<ImageDTO>> getProductImages(@PathVariable Long productId) {
        // Service 계층 호출 → DAO → Repository 순으로 DB 조회
        List<ImageDTO> images = productService.getProductImages(productId);

        // 조회 결과를 JSON 형태로 반환
        return ResponseEntity.ok(images);
    }
}