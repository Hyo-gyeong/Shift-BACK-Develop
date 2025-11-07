package com.project.shift.product.service;

import com.project.shift.product.dao.ProductDAO;
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

    private final ProductDAO productDAO; // DAO 의존성 주입

    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    /**
     * 전체 상품 목록을 조회하여 DTO로 변환 후 반환. (PROD-001)
     */
    @Override
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productDAO.findAll(); // DAO를 통해 상품 목록 조회
        return products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * 상품 상세 조회 (ID로 조회) (PROD-002)
     */
    @Override
    public ProductDTO getProductDetails(Long productId) {
        Product product = productDAO.findById(productId); // DAO를 통해 상품 조회
        return convertToDTO(product);
    }

    /**
     * 특정 카테고리에 속한 상품 목록을 조회하는 서비스 메소드입니다. (PROD-004)
     */
    @Override
    public List<ProductDTO> getProductsByCategory(Long categoryId) {
        List<Product> products = productDAO.findByCategory(categoryId); // DAO를 통해 카테고리별 상품 조회
        return products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * 상품을 저장하는 서비스 메소드입니다. (시퀀스를 사용하여 자동으로 ID 생성)
     * @param product 저장할 상품 객체
     */
    @Override
    public void saveProduct(Product product) {
        productDAO.saveProduct(product); // DAO를 통해 상품 저장
    }

    /**
     * Product 엔티티를 ProductDTO로 변환
     */
    private ProductDTO convertToDTO(Product product) {
        return new ProductDTO(
                product.getId().toString(),
                product.getName(),
                product.getPrice(),
                product.getStock(),
                product.getRegistrationDate().toString(),  // 등록 날짜
                product.getSeller(),
                product.getImages().stream()
                        .map(image -> image.getImageUrl())
                        .collect(Collectors.toList())
        );
    }
}