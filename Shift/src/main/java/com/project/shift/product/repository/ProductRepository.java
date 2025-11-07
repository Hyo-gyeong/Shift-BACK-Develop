package com.project.shift.product.repository;

import com.project.shift.product.entity.Category;
import com.project.shift.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 상품에 대한 CRUD 작업을 처리하는 리포지토리.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * 카테고리와 연결된 상품 목록을 조회하는 메서드.
     * @param category 카테고리 객체
     * @return 해당 카테고리에 속한 상품 목록
     */
    List<Product> findByCategory(Category category); // Category 객체를 기준으로 상품 조회
}