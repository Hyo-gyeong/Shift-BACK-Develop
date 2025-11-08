package com.project.shift.product.dao;

import com.project.shift.product.entity.Product;
import com.project.shift.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}