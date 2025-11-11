package com.project.shift.product.dao;

import com.project.shift.product.entity.Product;

import java.util.List;

import org.springframework.data.domain.Sort;

/**
 * Product 엔티티와 관련된 데이터베이스 작업을 정의한 DAO 인터페이스.
 */
public interface IProductDAO {

    /**
     * 모든 상품을 조회.	(PROD-001)
     * @return 상품 목록
     */
    List<Product> findAll(); // 상품 목록 조회 메서드 정의
    
    /**
     * 전체 조회에서 특정 카테고리를 제외. (PROD-001 확장)
     * 예: 금액권 카테고리(3) 제외.
     */
    List<Product> findAllExcludingCategory(Long excludedCategoryId);
    
    /**
     * 상품을 저장하는 메소드.
     * @param product 저장할 상품 객체
     */
    void saveProduct(Product product); // 상품 저장 메소드 정의

	/**
	 * 상품을 ID로 조회합니다. (PROD-002) / (PROD-009)
	 */
	Product findById(Long productId);

	/**
	 * 특정 카테고리에 속한 상품 목록을 조회합니다. (PROD-004)
	 */
	List<Product> findByCategory(Long categoryId);

	/** 
	 * 공백 없는 검색 처리 ("화이트디퓨저") (PROD-005)
	 */
	List<Product> searchIgnoringSpaces(String keyword);

	/**
	 * 공백 없는 검색 처리 + 특정 카테고리 제외 (PROD-005)
	 * 예: excludedCategoryId = 3L(금액권)
	 */
	List<Product> searchIgnoringSpacesExcludingCategory(String keyword, Long excludedCategoryId);

	/**
	 * 부분 단어 검색 ("화이트") (PROD-005)
	 */
	List<Product> searchByName(String productName);

	/**
	 * 부분 단어 검색 + 특정 카테고리 제외 (PROD-005)
	 * 예: excludedCategoryId = 3L(금액권)
	 */
	List<Product> searchByNameExcludingCategory(String productName, Long excludedCategoryId);

	/** 
	 * 공백 포함 AND 조건 검색 ("화이트 디퓨저") (PROD-005)
	 */
	List<Product> searchByMultipleKeywords(String[] keywords);

	/**
	 * 공백 포함 AND 조건 검색 + 특정 카테고리 제외 (PROD-005)
	 * 예: excludedCategoryId = 3L(금액권)
	 */
	List<Product> searchByMultipleKeywordsExcludingCategory(String[] keywords, Long excludedCategoryId);
	
	/**
     * 정렬 조건을 적용하여 전체 상품을 조회. (PROD-006)
     * 허용값 예시는 Service에서 관리.
     * 여기서는 DB 접근만 수행.
     */
    List<Product> findAllSorted(Sort sort);

    /**
     * 정렬 조회에서 특정 카테고리를 제외. (PROD-006 확장)
     * 예: 금액권 카테고리(3) 제외.
     */
    List<Product> findAllSortedExcludingCategory(Long excludedCategoryId, Sort sort);
    
    /** 
     * 카테고리 한정 정렬 조회. (PROD-004 + PROD-006)
    */
    List<Product> findByCategorySorted(Long categoryId, Sort sort);
}