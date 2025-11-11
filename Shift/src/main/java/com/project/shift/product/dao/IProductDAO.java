package com.project.shift.product.dao;

import com.project.shift.product.entity.Product;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * [DAO-INT-001] 상품 DAO 인터페이스
 * ---------------------------------------------------------
 * - ProductDAO의 메서드 시그니처 정의
 */
public interface IProductDAO {

    /** [PROD-001] 전체 상품 목록 조회 */
    List<Product> findAll();

    /** [PROD-001] 금액권 제외 전체 상품 조회 */
    List<Product> findAllExcludingCategory(Long excludedCategoryId);

    /** [PROD-002] 상품 상세 조회 */
    Product findById(Long productId);

    /** [PROD-004] 카테고리별 상품 조회 */
    List<Product> findByCategory(Long categoryId);

    /** [PROD-005] 상품명 부분 일치 검색 */
    List<Product> searchByName(String productName);

    /** [PROD-005] 상품명 부분 일치 검색 (금액권 제외) */
    List<Product> searchByNameExcludingCategory(String keyword, Long excludedCategoryId);

    /** [PROD-005] 공백 무시 검색 */
    List<Product> searchIgnoringSpaces(String keyword);

    /** [PROD-005] 공백 무시 검색 (금액권 제외) */
    List<Product> searchIgnoringSpacesExcludingCategory(String keyword, Long excludedCategoryId);

    /** [PROD-005] 다중 키워드 검색 (AND 조건) */
    List<Product> searchByMultipleKeywords(String[] keywords);

    /** [PROD-005] 다중 키워드 검색 (금액권 제외) */
    List<Product> searchByMultipleKeywordsExcludingCategory(String[] keywords, Long excludedCategoryId);

    /** [PROD-006] 전체 정렬 */
    List<Product> findAllSorted(Sort sort);

    /** [PROD-006] 금액권 제외 정렬 조회 */
    List<Product> findAllSortedExcludingCategory(Long excludedCategoryId, Sort sort);

    /** [PROD-004 + PROD-006] 카테고리 한정 정렬 상품 조회 */
    List<Product> findByCategorySorted(Long categoryId, Sort sort);

    /** [PROD-009] 상품 저장 (시퀀스 기반 자동 ID 생성) */
    void saveProduct(Product product);
}