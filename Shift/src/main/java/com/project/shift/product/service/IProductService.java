package com.project.shift.product.service;

import com.project.shift.product.dto.ImageDTO;
import com.project.shift.product.dto.ProductDTO;
import com.project.shift.product.entity.Product;
import java.util.List;

/**
 * [I-SERVICE-001] 상품 관련 서비스 인터페이스
 * ---------------------------------------------------------
 * - PROD-001 : 전체 상품 목록 조회
 * - PROD-002 : 상품 상세 조회
 * - PROD-004 : 카테고리별 상품 조회
 * - PROD-005 : 상품 검색
 * - PROD-006 : 상품 정렬
 * - PROD-007 : 상품 이미지 조회
 * - PROD-009 : 상품 재고 조회
 * ---------------------------------------------------------
 * ※ 금액권(Category_ID = 3)은 모든 일반 조회/검색/정렬에서 제외
 */
public interface IProductService {

    /** [PROD-001] 전체 상품 목록 조회 */
    List<ProductDTO> getAllProducts();

    /** [PROD-002] 상품 상세 조회 */
    ProductDTO getProductDetails(Long productId);

    /** [PROD-004] 카테고리별 상품 목록 조회 */
    List<ProductDTO> getProductsByCategory(Long categoryId);

    /** [PROD-005] 상품명 검색 (부분 일치) */
    List<ProductDTO> searchProducts(String keyword);

    /** [PROD-006] 상품 정렬 (가격순/최신순) */
    List<ProductDTO> getSortedProducts(String sortType);

    /** [PROD-004 + PROD-006] 카테고리 한정 정렬 상품 조회 */
    List<ProductDTO> getProductsByCategorySorted(Long categoryId, String sortType);

    /** [PROD-007] 상품 이미지 목록 조회 */
    List<ImageDTO> getProductImages(Long productId);

    /** [PROD-009] 상품 재고 조회 */
    ProductDTO getProductStock(Long productId);

    /** 상품 저장 (시퀀스 기반 자동 ID 생성) */
    void saveProduct(Product product);
}