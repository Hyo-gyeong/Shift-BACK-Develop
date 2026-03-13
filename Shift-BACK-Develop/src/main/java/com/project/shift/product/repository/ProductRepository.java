package com.project.shift.product.repository;

import com.project.shift.product.entity.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

/**
 * [REP-001] 상품 Repository
 * ---------------------------------------------------------
 * - 엔티티 필드명(name) 기준
 * - 공백 포함/무시/유연(가변간격) 검색 통합 지원
 * ---------------------------------------------------------
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    /** [PROD-001] 카테고리 제외 전체 조회 */
    List<Product> findByCategory_CategoryIdNot(Long excludedCategoryId);

    /** [PROD-001] 카테고리 제외 전체 조회 + 정렬 */
    List<Product> findByCategory_CategoryIdNot(Long excludedCategoryId, Sort sort);

    /** [PROD-004] 카테고리별 상품 조회 */
    List<Product> findByCategory_CategoryId(Long categoryId);

    /** [PROD-004 + PROD-006] 카테고리별 정렬 조회 */
    List<Product> findByCategory_CategoryId(Long categoryId, Sort sort);

    /** [PROD-005] 부분 일치 검색 */
    List<Product> findByNameContainingIgnoreCase(String name);

    /** [PROD-005] 부분 일치 검색 (금액권 제외) */
    List<Product> findByNameContainingIgnoreCaseAndCategory_CategoryIdNot(String name, Long excludedCategoryId);

    /** [PROD-005] 공백 무시 검색 */
    @Query("""
        SELECT p FROM Product p
        WHERE REPLACE(LOWER(p.name), ' ', '') LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    List<Product> findByNameIgnoreSpaces(@Param("keyword") String keyword);

    /** [PROD-005] 공백 무시 검색 (금액권 제외) */
    @Query("""
        SELECT p FROM Product p
        WHERE p.category.categoryId <> :excludedCategoryId
          AND REPLACE(LOWER(p.name), ' ', '') LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    List<Product> findByNameIgnoreSpacesAndCategory_CategoryIdNot(
            @Param("keyword") String keyword,
            @Param("excludedCategoryId") Long excludedCategoryId
    );

    /** [PROD-005] 다중 키워드 검색 (AND 조건) */
    @Query("""
        SELECT p FROM Product p
        WHERE (:#{#keywords.length} = 0 OR (
            LOWER(p.name) LIKE LOWER(CONCAT('%', :#{#keywords[0]}, '%'))
            AND (:#{#keywords.length} < 2 OR LOWER(p.name) LIKE LOWER(CONCAT('%', :#{#keywords[1]}, '%')))
            AND (:#{#keywords.length} < 3 OR LOWER(p.name) LIKE LOWER(CONCAT('%', :#{#keywords[2]}, '%')))
        ))
    """)
    List<Product> findByMultipleKeywords(@Param("keywords") String[] keywords);

    /** [PROD-005] 다중 키워드 검색 (금액권 제외) */
    @Query("""
        SELECT p FROM Product p
        WHERE p.category.categoryId <> :excludedCategoryId
          AND (:#{#keywords.length} = 0 OR (
            LOWER(p.name) LIKE LOWER(CONCAT('%', :#{#keywords[0]}, '%'))
            AND (:#{#keywords.length} < 2 OR LOWER(p.name) LIKE LOWER(CONCAT('%', :#{#keywords[1]}, '%')))
            AND (:#{#keywords.length} < 3 OR LOWER(p.name) LIKE LOWER(CONCAT('%', :#{#keywords[2]}, '%')))
          ))
    """)
    List<Product> findByMultipleKeywordsExcludingCategory(
            @Param("keywords") String[] keywords,
            @Param("excludedCategoryId") Long excludedCategoryId
    );


    /* ============================================================
       ✅ 통합 검색 (공백 포함 + 무시 + 가변간격 일치 모두 처리)
       ============================================================ */

    /** [PROD-005] 공백 포함/무시/가변간격 통합 검색 */
    @Query("""
        SELECT p FROM Product p
        WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', REPLACE(:keyword, ' ', '%'), '%'))
           OR REPLACE(LOWER(p.name), ' ', '') LIKE LOWER(CONCAT('%', :normalized, '%'))
    """)
    List<Product> searchFlexible(
            @Param("keyword") String keyword,
            @Param("normalized") String normalized
    );

    /** [PROD-005] 공백 포함/무시/가변간격 통합 검색 (금액권 제외) */
    @Query("""
        SELECT p FROM Product p
        WHERE p.category.categoryId <> :excludedCategoryId
          AND (
              LOWER(p.name) LIKE LOWER(CONCAT('%', REPLACE(:keyword, ' ', '%'), '%'))
           OR REPLACE(LOWER(p.name), ' ', '') LIKE LOWER(CONCAT('%', :normalized, '%'))
          )
    """)
    List<Product> searchFlexibleExcludingCategory(
            @Param("keyword") String keyword,
            @Param("normalized") String normalized,
            @Param("excludedCategoryId") Long excludedCategoryId
    );
    
    @Query("SELECT p.category.categoryId FROM Product p WHERE p.id = :productId")
    Long findCategoryIdByProductId(@Param("productId") Long productId);

}