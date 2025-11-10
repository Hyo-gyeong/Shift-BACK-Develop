package com.project.shift.product.dao;

import com.project.shift.product.entity.Product;
import com.project.shift.product.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Product 엔티티와 관련된 데이터베이스 작업을 처리하는 DAO 클래스.
 */
@Repository
public class ProductDAO implements IProductDAO {

    private final ProductRepository productRepository; // Repository 의존성 주입

    @Autowired
    public ProductDAO(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * 전체 상품을 조회. (PROD-001)
     */
    @Override
    public List<Product> findAll() {
        return productRepository.findAll(); // Repository에서 모든 상품 조회
    }
    
    /**
     * 전체 조회에서 특정 카테고리를 제외한다. (PROD-001 확장)
     * 예) 금액권(카테고리 3) 제외.
     *
     * @param excludedCategoryId 제외할 카테고리 ID
     * @return 제외 카테고리를 뺀 전체 상품 목록
     */
    @Override
    public List<Product> findAllExcludingCategory(Long excludedCategoryId) { // (ADD)
        // DB 접근만 수행. 비즈니스 규칙은 Service에서 결정.
        return productRepository.findByCategory_CategoryIdNot(excludedCategoryId);
    }

    /**
     * 상품을 ID로 조회합니다. (PROD-002) - (PROD-009)
     */
    @Override
    public Product findById(Long productId) {
        return productRepository.findById(productId).orElse(null);
    }

    /**
     * 특정 카테고리에 속한 상품 목록을 조회합니다. (PROD-004)
     */
    @Override
    public List<Product> findByCategory(Long categoryId) {
        return productRepository.findByCategory_CategoryId(categoryId); // 카테고리별 상품 조회
    }

    /**
     * 상품을 저장합니다. (시퀀스를 사용하여 자동으로 ID 생성)
     * @param product 저장할 상품 객체
     */
    @Override
    public void saveProduct(Product product) {
        productRepository.save(product); // Repository를 통해 상품 저장
    }
    
    /**
     * 공백 없는 검색 처리 ("화이트디퓨저") (PROD-005)
     * - 전처리: 공백 제거 + 소문자화
     * - DB: REGEXP_LIKE(REPLACE(LOWER(PRODUCT_NAME),' ',''), :pattern)
     */
    @Override
    public List<Product> searchIgnoringSpaces(String keyword) {
        final String k = keyword == null ? "" : keyword.replaceAll("\\s+", "").toLowerCase();
        if (k.isEmpty()) return List.of();

        // "화이트디퓨저" → ".*화.*이.*트.*디.*퓨.*저.*"
        final StringBuilder sb = new StringBuilder(".*");
        k.chars().forEach(ch -> sb.append((char) ch).append(".*"));
        final String regex = sb.toString();

        return productRepository.searchIgnoringSpacesSubsequence(regex);
    }

    /**
     * 공백 없는 검색 처리 + 특정 카테고리 제외 (PROD-005)
     * 예: excludedCategoryId = 3L(금액권)
     */
    @Override
    public List<Product> searchIgnoringSpacesExcludingCategory(String keyword, Long excludedCategoryId) {
        final String k = keyword == null ? "" : keyword.replaceAll("\\s+", "").toLowerCase();
        if (k.isEmpty()) return List.of();

        final StringBuilder sb = new StringBuilder(".*");
        k.chars().forEach(ch -> sb.append((char) ch).append(".*"));
        final String regex = sb.toString();

        return productRepository.searchIgnoringSpacesSubsequenceExcludingCategory(regex, excludedCategoryId);
    }

    /**
     * 부분 단어 검색 ("화이트") (PROD-005)
     * - 대소문자 무시
     */
    @Override
    public List<Product> searchByName(String productName) {
        final String key = productName == null ? "" : productName.trim();
        if (key.isEmpty()) return List.of();
        return productRepository.findByNameContainingIgnoreCase(key);
    }

    /**
     * 부분 단어 검색 + 특정 카테고리 제외 (PROD-005)
     * 예: excludedCategoryId = 3L(금액권)
     */
    @Override
    public List<Product> searchByNameExcludingCategory(String productName, Long excludedCategoryId) {
        final String key = productName == null ? "" : productName.trim();
        if (key.isEmpty()) return List.of();
        return productRepository.findByNameContainingIgnoreCaseAndCategory_CategoryIdNot(key, excludedCategoryId);
    }

    /**
     * 공백 포함 AND 조건 검색 ("화이트 디퓨저") (PROD-005)
     * - 간단 구현: 메모리 AND 필터
     * - 데이터가 많으면 JPQL/네이티브로 변경 권장
     */
    @Override
    public List<Product> searchByMultipleKeywords(String[] keywords) {
        if (keywords == null || keywords.length == 0) return List.of();

        final String[] terms = Arrays.stream(keywords)
                .filter(s -> s != null)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(String::toLowerCase)
                .toArray(String[]::new);

        if (terms.length == 0) return List.of();

        return productRepository.findAll().stream()
                .filter(p -> {
                    final String name = p.getName() == null ? "" : p.getName().toLowerCase();
                    for (String t : terms) if (!name.contains(t)) return false;
                    return true;
                })
                .toList();
    }

    /**
     * 공백 포함 AND 조건 검색 + 특정 카테고리 제외 (PROD-005)
     * 예: excludedCategoryId = 3L(금액권)
     * - 간단 구현: 카테고리 제외 후 메모리 AND 필터
     * - 성능 필요 시 쿼리로 치환
     */
    @Override
    public List<Product> searchByMultipleKeywordsExcludingCategory(String[] keywords, Long excludedCategoryId) {
        if (keywords == null || keywords.length == 0) return List.of();

        final String[] terms = Arrays.stream(keywords)
                .filter(s -> s != null)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(String::toLowerCase)
                .toArray(String[]::new);

        if (terms.length == 0) return List.of();

        return productRepository.findByCategory_CategoryIdNot(excludedCategoryId).stream()
                .filter(p -> {
                    final String name = p.getName() == null ? "" : p.getName().toLowerCase();
                    for (String t : terms) if (!name.contains(t)) return false;
                    return true;
                })
                .toList();
    }
    
    /**
     * 정렬 조건을 적용하여 전체 상품을 조회. (PROD-006)
     * 정렬 키는 엔티티 필드명(price, registrationDate, id)으로 전달해야 함.
     */
    @Override
    public List<Product> findAllSorted(Sort sort) {
        return productRepository.findAll(sort);
    }

    /**
     * 정렬 조회에서 특정 카테고리를 제외한다. (PROD-006 확장)
     * 예) 금액권(카테고리 3) 제외 + 정렬 조건 적용.
     *
     * @param excludedCategoryId 제외할 카테고리 ID
     * @param sort               정렬 조건(엔티티 필드명 기준: price, registrationDate, id)
     * @return 제외 카테고리를 뺀 정렬된 상품 목록
     */
    @Override
    public List<Product> findAllSortedExcludingCategory(Long excludedCategoryId, Sort sort) { // (ADD)
        // Repository의 findAll(Sort) 변형 시그니처를 사용해 정렬 적용.
        return productRepository.findByCategory_CategoryIdNot(excludedCategoryId, sort);
    }
    
    /** 
     * 카테고리 한정 정렬 조회. (PROD-004 + PROD-006)
    */
    @Override
    public List<Product> findByCategorySorted(Long categoryId, Sort sort) {
        return productRepository.findByCategory_CategoryId(categoryId, sort);
    }
}