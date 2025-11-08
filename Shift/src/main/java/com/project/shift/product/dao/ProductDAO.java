package com.project.shift.product.dao;

import com.project.shift.product.entity.Product;
import com.project.shift.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
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
     * 상품을 ID로 조회합니다. (PROD-002)
     */
    @Override
    public Product findById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found")); // ID로 상품 조회
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
     * 공백 없는 검색어 → 서브시퀀스 정규식으로 매칭 (PROD-005)
    */ 
    public List<Product> searchIgnoringSpaces(String keyword) {
        // 1) 공백 제거 + 소문자화
        String k = keyword == null ? "" : keyword.replaceAll("\\s+", "").toLowerCase();
        if (k.isEmpty()) return List.of();

        // 2) 글자 사이에 ".*" 삽입 → 서브시퀀스 패턴 생성
        //    "화이트디퓨저" → ".*화.*이.*트.*디.*퓨.*저.*"
        StringBuilder sb = new StringBuilder(".*");
        k.chars().forEach(ch -> sb.append((char) ch).append(".*"));
        String regex = sb.toString();

        return productRepository.searchIgnoringSpacesSubsequence(regex);
    }

    // 부분 단어
    public List<Product> searchByName(String productName) {
        return productRepository.findByNameContainingIgnoreCase(productName);
    }

    // AND 조건
    @Override
    public List<Product> searchByMultipleKeywords(String[] keywords) {
        List<Product> all = productRepository.findAll();
        return all.stream()
                .filter(p -> java.util.Arrays.stream(keywords)
                        .allMatch(k -> p.getName().toLowerCase().contains(k.toLowerCase())))
                .toList();
    }
}