package com.project.shift.product.service;

import com.project.shift.product.dto.ProductDTO;
import com.project.shift.product.entity.Product;
import com.project.shift.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 상품 목록을 처리하는 서비스.
 */
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * 전체 상품 목록을 조회하여 DTO로 변환 후 반환.
     */
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll(); // 데이터베이스에서 모든 상품 조회
        return products.stream()
                .map(product -> new ProductDTO(
                        product.getId().toString(),
                        product.getName(),
                        product.getPrice(),
                        product.getStock(),
                        // 카테고리명과 이미지 URL 제외
                        null, // 카테고리명 부분은 null로 설정 - 기능 없는 오류 문제로 null 처리
                        null  // 이미지 URL도 null로 설정 - 기능 없는 오류 문제로 null 처리
                ))
                .collect(Collectors.toList()); // DTO 리스트로 변환 후 반환
    }

    // 카테고리명과 이미지 URL을 포함하는 메서드 삭제
}