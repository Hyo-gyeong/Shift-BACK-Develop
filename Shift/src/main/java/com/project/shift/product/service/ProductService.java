package com.project.shift.product.service;

import com.project.shift.product.dto.ProductDTO;
import com.project.shift.product.entity.Product;
import com.project.shift.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * 전체 상품 조회
     */
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * 카테고리별 상품 조회
     */
    public List<ProductDTO> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategory_CategoryId(categoryId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Product 엔티티 → ProductDTO 변환
     */
    private ProductDTO convertToDTO(Product product) {
        String productId = product.getId() != null ? product.getId().toString() : null;
        String productName = product.getName();
        int price = product.getPrice();
        int stock = product.getStock();
        String categoryName = (product.getCategory() != null)
                ? product.getCategory().getCategoryName()
                : null;
        String imageUrl = product.getFirstImageUrl();

        return new ProductDTO(productId, productName, price, stock, categoryName, imageUrl);
    }
}
