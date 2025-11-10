package com.project.shift.product.repository;

import com.project.shift.product.entity.Product;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 상품에 대한 CRUD 작업을 처리하는 리포지토리.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * 카테고리 ID를 기준으로 상품 목록을 조회하는 메서드.
     * @param categoryId 카테고리 ID	(PROD-004)
     * @return 해당 카테고리에 속한 상품 목록
     */
    List<Product> findByCategory_CategoryId(Long categoryId); // 카테고리의 categoryId를 기준으로 상품 조회
    
    /**
     * 상품명 부분 일치 검색 (대소문자 무시) (PROD-005)
     */
    List<Product> findByNameContainingIgnoreCase(String name);

    /**
     * 상품명 부분 일치 + 금액권 제외 (카테고리 ID != 3) (PROD-005)
     */
    List<Product> findByNameContainingIgnoreCaseAndCategory_CategoryIdNot(String name, Long excludedCategoryId);

    /**
     * 공백 무시 + 서브시퀀스 매칭 (PROD-005)
     * ex) keyword="화이트디퓨저" → ".*화.*이.*트.*디.*퓨.*저.*"
     */
    @Query(value = """
        SELECT *
          FROM PRODUCTS p
         WHERE REGEXP_LIKE(
                   REPLACE(LOWER(p.PRODUCT_NAME), ' ', ''),
                   :pattern
               )
        """, nativeQuery = true)
    List<Product> searchIgnoringSpacesSubsequence(@Param("pattern") String regexPattern);

    /**
     * 공백 무시 + 서브시퀀스 매칭 + 금액권 제외 (PROD-005)
     */
    @Query(value = """
        SELECT *
          FROM PRODUCTS p
         WHERE p.CATEGORY_ID <> :excluded
           AND REGEXP_LIKE(
                   REPLACE(LOWER(p.PRODUCT_NAME), ' ', ''),
                   :pattern
               )
        """, nativeQuery = true)
    List<Product> searchIgnoringSpacesSubsequenceExcludingCategory(@Param("pattern") String regexPattern,
                                                                   @Param("excluded") Long excludedCategoryId);
    
    /**
     * 카테고리 제외 전체 목록 (PROD-006)
     * @param categoryId
     * @return
     */
    List<Product> findByCategory_CategoryIdNot(Long categoryId);

    /**
     *  카테고리 제외 + 정렬 (PROD-006)
     * @param categoryId
     * @param sort
     * @return
     */
    List<Product> findByCategory_CategoryIdNot(Long categoryId, Sort sort);
    
    /** 
     * 카테고리 ID로 정렬 가능 조회 (PROD-004 + PROD+006)
     * @param categoryId
     * @param sort
     * @return
     */
    List<Product> findByCategory_CategoryId(Long categoryId, Sort sort);
}