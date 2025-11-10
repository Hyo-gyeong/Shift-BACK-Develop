package com.project.shift.product.service;

import com.project.shift.product.dto.ProductDTO;
import com.project.shift.product.entity.Product;

import java.util.List;

/**
 * 상품 관련 서비스 인터페이스.
 */
public interface IProductService {

    /**
     * 전체 상품 목록을 조회하는 메서드. (PROD-001)
     * @return 상품 목록
     */
    List<ProductDTO> getAllProducts(); // 상품 목록 조회 메서드 정의
    
    /**
     * 특정 카테고리에 속한 상품 목록을 조회하는 메서드.
     * @param categoryId 카테고리 ID 	(PROD-004)
     * @return 해당 카테고리에 속한 상품 목록
     */
    List<ProductDTO> getProductsByCategory(Long categoryId);
    
    /**
     * 상품 상세 조회 (ID로 조회)
     * @param productId 상품 ID	(PROD-002)
     * @return 상품 상세 정보
     */
    ProductDTO getProductDetails(Long productId); // 상품 상세 조회 메서드 정의

	/**
	 * 상품을 저장하는 서비스 메소드입니다. (시퀀스를 사용하여 자동으로 ID 생성)
	 * @param product 저장할 상품 객체
	 */
	void saveProduct(Product product);

	/** 
	 * 상품 검색 로직 (PROD-005)
	*/
	List<ProductDTO> searchProducts(String keyword);
	
	/**
     * 정렬 조건에 따른 상품 목록 조회. (PROD-006)
     * 허용값: priceAsc | priceDesc | latest
     * null/빈값은 latest 처리.
     */
    List<ProductDTO> getSortedProducts(String sortType);
    
    /**
     * 카테고리 한정 정렬 조회. (PROD-004 + PROD-006)
     * 허용값: priceAsc | priceDesc | latest
     * null/빈값은 latest 처리.
     *
     * @param categoryId 카테고리 ID
     * @param sortType   정렬 타입 문자열
     * @return 정렬된 상품 DTO 목록
     */
    List<ProductDTO> getProductsByCategorySorted(Long categoryId, String sortType);
}