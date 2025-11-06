package com.project.shift.product.service;

import com.project.shift.product.dto.ProductDTO;

import java.util.List;

/**
 * 상품 관련 서비스 인터페이스.
 */
public interface IProductService {
    List<ProductDTO> getAllProducts(); // 상품 목록 조회 메서드 정의
}