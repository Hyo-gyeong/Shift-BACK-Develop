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
     * 모든 상품을 조회.
     */
    @Override
    public List<Product> findAll() {
        return productRepository.findAll(); // ProductRepository를 이용해 모든 상품 조회
    }
}