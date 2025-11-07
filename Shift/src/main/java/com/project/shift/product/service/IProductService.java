package com.project.shift.product.service;

import com.project.shift.product.dto.ProductDTO;

import java.util.List;

/**
 * 상품 관련 서비스 인터페이스.
 */
public interface IProductService {

    /**
     * 전체 상품 목록을 조회하는 메서드.
     * @return 상품 목록
     */
    List<ProductDTO> getAllProducts(); // 상품 목록 조회 메서드 정의
    
    /**
     * 특정 카테고리에 속한 상품 목록을 조회하는 메서드.
     * @param categoryId 카테고리 ID
     * @return 해당 카테고리에 속한 상품 목록
     */
    List<ProductDTO> getProductsByCategory(Long categoryId);
}