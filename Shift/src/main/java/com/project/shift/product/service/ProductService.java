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
                        // product.getCategory().getCategoryName(), // 카테고리명 포함
                        getImageUrl(product) // 이미지 URL 부분 - 이미지 조회 시 수정 필요
                ))
                .collect(Collectors.toList()); // DTO 리스트로 변환 후 반환
    }

    /**
     * 상품의 이미지 URL을 조회하는 메서드 (대표 이미지).
     */
//    private String getImageUrl(Product product) {
//        if (product.getImages() != null && !product.getImages().isEmpty()) {
//            return product.getImages().get(0).getImageUrl(); // 첫 번째 이미지 URL 반환
//        }
//        return null; // 이미지가 없으면 null 반환
//    }
}