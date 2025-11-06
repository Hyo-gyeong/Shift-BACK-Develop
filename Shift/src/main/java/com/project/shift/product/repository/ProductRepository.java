package com.project.shift.product.repository;

import com.project.shift.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;



/**
 * 상품에 대한 CRUD 작업을 처리하는 리포지토리.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    // 상품 목록을 조회하는 기본 메서드 (findAll은 JpaRepository에서 기본 제공)
    List<Product> findAll();

    // 카테고리별
    List<Product> findByCategory_CategoryId(Long categoryId);

}
