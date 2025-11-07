package com.project.shift.product.service;

import com.project.shift.product.dto.ProductDTO;
import com.project.shift.product.entity.Product;
import com.project.shift.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 상품 목록을 처리하는 서비스.
 */
@Service
public class ProductService implements IProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * 전체 상품 목록을 조회하여 DTO로 변환 후 반환. (PROD-001)
     */
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll(); // 데이터베이스에서 모든 상품 조회
        return products.stream()
                .map(product -> new ProductDTO(
                        product.getId().toString(),
                        product.getName(),
                        product.getPrice(),
                        product.getStock(),
                        product.getRegistrationDate().toString(),  // 등록 날짜
                        product.getSeller(),
                        product.getImages().stream()
                                .map(image -> image.getImageUrl())
                                .collect(Collectors.toList()) // 이미지 URL 목록
                ))
                .collect(Collectors.toList()); // DTO 리스트로 변환 후 반환
    }

    /**
     * 상품 상세 조회 (ID로 조회) (PROD-002)
     */
    public ProductDTO getProductDetails(Long productId) {
        Optional<Product> productOpt = productRepository.findById(productId); // 상품 ID로 조회

        // 상품이 존재하면 DTO로 변환하여 반환, 없으면 예외 처리
        return productOpt.map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Product not found")); // 예외 처리
    }

    /**
     * 특정 카테고리에 속한 상품 목록을 조회하는 서비스 메소드입니다. (PROD-004)
     * @param categoryId 카테고리 ID 
     * @return 해당 카테고리에 속한 상품 목록
     */
    public List<ProductDTO> getProductsByCategory(Long categoryId) {
        // 카테고리 ID로 상품 목록 조회
        List<Product> products = productRepository.findByCategory_CategoryId(categoryId); // 수정된 메소드 사용

        return products.stream()
                .map(product -> new ProductDTO(
                        product.getId().toString(),
                        product.getName(),
                        product.getPrice(),
                        product.getStock(),
                        product.getRegistrationDate().toString(),  // 등록 날짜
                        product.getSeller(),
                        product.getImages().stream()
                                .map(image -> image.getImageUrl())  // 이미지 URL 목록 추가
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList()); // DTO 리스트로 변환 후 반환
    }

    /**
     * 상품을 저장하는 서비스 메소드입니다. (시퀀스를 사용하여 자동으로 ID 생성)
     * @param product 저장할 상품 객체
     */
    public void saveProduct(Product product) {
        productRepository.save(product); // JPA가 시퀀스를 사용하여 자동 ID 생성
    }

    /**
     * Product 엔티티를 ProductDTO로 변환
     */
    private ProductDTO convertToDTO(Product product) {
        ProductDTO productDTO = new ProductDTO(
                product.getId().toString(),
                product.getName(),
                product.getPrice(),
                product.getStock(),
                product.getRegistrationDate().toString(),  // 등록 날짜
                product.getSeller(),
                product.getImages().stream()
                        .map(image -> image.getImageUrl())  // 이미지 URL 목록 추가
                        .collect(Collectors.toList())
        );
        return productDTO;
    }
}