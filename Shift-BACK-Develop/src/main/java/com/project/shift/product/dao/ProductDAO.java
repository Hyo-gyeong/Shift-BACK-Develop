package com.project.shift.product.dao;

import com.project.shift.product.entity.Product;
import com.project.shift.product.repository.ProductRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * [DAO-001] 상품 관련 데이터 접근 클래스
 * ---------------------------------------------------------
 * - PROD-001 : 전체 상품 목록 조회
 * - PROD-002 : 상품 상세 조회
 * - PROD-004 : 카테고리별 상품 조회
 * - PROD-005 : 상품 검색 (공백포함/무시/다중키워드)
 * - PROD-006 : 상품 정렬
 * ---------------------------------------------------------
 * ※ 금액권(Category_ID = 3)은 모든 일반 조회/검색/정렬에서 제외
 */
@Repository
public class ProductDAO implements IProductDAO {

    private final ProductRepository productRepository;

    public ProductDAO(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /** [PROD-001] 전체 상품 목록 조회 */
    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    /** [PROD-001] 금액권 제외 전체 상품 조회 */
    @Override
    public List<Product> findAllExcludingCategory(Long excludedCategoryId) {
        return productRepository.findByCategory_CategoryIdNot(excludedCategoryId);
    }

    /** [PROD-002] 상품 ID로 상세 조회 */
    @Override
    public Product findById(Long productId) {
        return productRepository.findById(productId).orElse(null);
    }

    /** [PROD-004] 카테고리별 상품 조회 */
    @Override
    public List<Product> findByCategory(Long categoryId) {
        return productRepository.findByCategory_CategoryId(categoryId);
    }

    /** [PROD-005] 상품명 부분 일치 검색 (공백 자동 분기) */
    @Override
    public List<Product> searchByName(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) return List.of();

        String trimmed = keyword.trim();
        boolean hasSpace = trimmed.contains(" ");

        if (hasSpace) {
            // 공백 포함 → 부분 일치 검색
            return productRepository.findByNameContainingIgnoreCase(trimmed);
        } else {
            // 공백 제거 후 비교
            String normalized = trimmed.replaceAll("\\s+", "");
            return productRepository.findByNameIgnoreSpaces(normalized);
        }
    }

    /** [PROD-005] 상품명 부분 일치 검색 (금액권 제외, 공백 자동 분기) */
    @Override
    public List<Product> searchByNameExcludingCategory(String keyword, Long excludedCategoryId) {
        if (keyword == null || keyword.trim().isEmpty()) return List.of();

        String trimmed = keyword.trim();
        String normalized = trimmed.replaceAll("\\s+", "");
        
        // ✅ 무조건 통합검색 메서드로 통일
        return productRepository.searchFlexibleExcludingCategory(trimmed, normalized, excludedCategoryId);
    }

    /** [PROD-005] 공백 무시 검색 (단독 호출용) */
    @Override
    public List<Product> searchIgnoringSpaces(String keyword) {
        String normalized = keyword.replaceAll("\\s+", "");
        return productRepository.findByNameIgnoreSpaces(normalized);
    }

    /** [PROD-005] 공백 무시 검색 (금액권 제외, 단독 호출용) */
    @Override
    public List<Product> searchIgnoringSpacesExcludingCategory(String keyword, Long excludedCategoryId) {
        String normalized = keyword.replaceAll("\\s+", "");
        return productRepository.findByNameIgnoreSpacesAndCategory_CategoryIdNot(normalized, excludedCategoryId);
    }

    /** [PROD-005] 다중 키워드 검색 (AND 조건) */
    @Override
    public List<Product> searchByMultipleKeywords(String[] keywords) {
        return productRepository.findByMultipleKeywords(keywords);
    }

    /** [PROD-005] 다중 키워드 검색 (금액권 제외) */
    @Override
    public List<Product> searchByMultipleKeywordsExcludingCategory(String[] keywords, Long excludedCategoryId) {
        return productRepository.findByMultipleKeywordsExcludingCategory(keywords, excludedCategoryId);
    }

    /** [PROD-006] 전체 정렬 */
    @Override
    public List<Product> findAllSorted(Sort sort) {
        return productRepository.findAll(sort);
    }

    /** [PROD-006] 금액권 제외 + 정렬 적용 전체 조회 */
    @Override
    public List<Product> findAllSortedExcludingCategory(Long excludedCategoryId, Sort sort) {
        return productRepository.findByCategory_CategoryIdNot(excludedCategoryId, sort);
    }

    /** [PROD-004 + PROD-006] 카테고리 한정 정렬 상품 조회 */
    @Override
    public List<Product> findByCategorySorted(Long categoryId, Sort sort) {
        return productRepository.findByCategory_CategoryId(categoryId, sort);
    }

    /** 상품 저장 (시퀀스 기반 자동 ID 생성) */
    @Override
    public void saveProduct(Product product) {
        productRepository.save(product);
    }
}