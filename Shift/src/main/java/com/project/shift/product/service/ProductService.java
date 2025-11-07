package com.project.shift.product.service;

import com.project.shift.product.dto.ProductDTO;
import com.project.shift.product.entity.Category;
import com.project.shift.product.entity.Product;
import com.project.shift.product.repository.ProductRepository;
import org.springframework.stereotype.Service;  // 이 부분 추가

import java.util.List;
import java.util.stream.Collectors;

/**
 * 상품 목록을 처리하는 서비스.
 */
@Service  // 이 어노테이션이 없으면 빈으로 등록되지 않음
public class ProductService implements IProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * 특정 카테고리에 속한 상품 목록을 조회하는 서비스 메소드입니다.
     * @param categoryId 카테고리 ID
     * @return 해당 카테고리에 속한 상품 목록
     */
    public List<ProductDTO> getProductsByCategory(Long categoryId) {
        Category category = new Category();
        category.setCategoryId(categoryId); // categoryId로 카테고리 객체 생성

        List<Product> products = productRepository.findByCategory(category); // 카테고리로 상품 조회

        return products.stream()
                .map(product -> new ProductDTO(
                        product.getId().toString(),
                        product.getName(),
                        product.getPrice(),
                        product.getStock()
                ))
                .collect(Collectors.toList()); // DTO 리스트로 변환 후 반환
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
                        product.getStock()
                ))
                .collect(Collectors.toList()); // DTO 리스트로 변환 후 반환
    }
}