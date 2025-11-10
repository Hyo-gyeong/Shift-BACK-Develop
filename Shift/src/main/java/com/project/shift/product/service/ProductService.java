package com.project.shift.product.service;

import com.project.shift.product.dao.ProductDAO;
import com.project.shift.product.dto.ProductDTO;
import com.project.shift.product.entity.Product;
import com.project.shift.product.repository.ProductRepository;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
     * 전체 상품 목록 조회. (PROD-001)
     * 정책: 전체 조회에서 금액권(카테고리 ID = 3)은 제외한다.
     *
     * 구현 메모:
     * - 카테고리 제외 여부 같은 비즈니스 규칙은 Service에서 결정하고,
     *   DAO는 DB 접근만 수행한다.
     * - DTO 변환은 기존 convertToDTO를 그대로 재사용한다.
     *
     * @return 금액권을 제외한 전체 상품 DTO 목록
     */
    @Override
    public List<ProductDTO> getAllProducts() { // PROD-001: 전체 조회에서 카테고리 3 제외
        // 1) DAO에서 카테고리 3을 제외한 엔티티 목록을 가져온다.
        List<Product> products = productDAO.findAllExcludingCategory(3L);

        // 2) 엔티티 → DTO 매핑. 기존 변환 로직 재사용.
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
     * 상품 검색 로직 (PROD-005)
     * 정책: 검색 결과에서도 금액권(카테고리 ID = 3) 제외.
     */
    @Override
    public List<ProductDTO> searchProducts(String keyword) {
        keyword = (keyword == null) ? "" : keyword.trim();
        if (keyword.isEmpty()) return List.of();

        final long EXCLUDED = 3L;
        List<Product> results = new ArrayList<>();

        // ① 공백 없는 검색어
        if (!keyword.contains(" ")) {
            // 공백 무시 서브시퀀스
            results.addAll(productDAO.searchIgnoringSpacesExcludingCategory(keyword, EXCLUDED));
            // 부분 일치
            results.addAll(productDAO.searchByNameExcludingCategory(keyword, EXCLUDED));
        }
        // ② 공백 포함 AND 조건
        else {
            String[] words = Arrays.stream(keyword.split("\\s+"))
                    .filter(s -> !s.isBlank())
                    .toArray(String[]::new);
            results.addAll(productDAO.searchByMultipleKeywordsExcludingCategory(words, EXCLUDED));
        }

        // 중복 제거 후 DTO 변환
        return results.stream()
                .collect(Collectors.toMap(Product::getId, p -> p, (a, b) -> a, java.util.LinkedHashMap::new))
                .values().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 정렬 조건에 따른 상품 목록 조회. (PROD-006)
     * 허용값: priceAsc | priceDesc | latest
     * null/빈값은 latest 처리.
     *
     * 정책:
     * - 전체 정렬에서도 금액권(카테고리 ID = 3)은 제외한다.
     *
     * 정렬 규칙:
     *  - priceAsc  : price ASC, id ASC
     *  - priceDesc : price DESC, id ASC
     *  - latest    : registrationDate DESC, id DESC
     *
     * 주의:
     *  - 정렬 키는 엔티티 필드명으로 지정해야 한다(price, registrationDate, id).
     *  - DB 컬럼명(PRICE, REGISTRATION_DATE, PRODUCT_ID)과 매핑됨.
     *
     * @param sortType 정렬 타입 문자열
     * @return 금액권을 제외한 정렬된 상품 DTO 목록
     */
    @Override
    public List<ProductDTO> getSortedProducts(String sortType) { // PROD-006: 전체 정렬에서 카테고리 3 제외
        // 1) 정렬 타입 정규화: null/빈값은 latest
        final String key = (sortType == null || sortType.isBlank()) ? "latest" : sortType.trim();

        // 2) 정렬 전략 구성: 동가/동일일자 안정성을 위해 id를 2차 키로 사용
        final Sort sort = switch (key) {
            case "priceAsc"  -> Sort.by(Sort.Order.asc("price"),  Sort.Order.asc("id"));
            case "priceDesc" -> Sort.by(Sort.Order.desc("price"), Sort.Order.asc("id"));
            default          -> Sort.by(Sort.Order.desc("registrationDate"), Sort.Order.desc("id"));
        };

        // 3) DAO에서 카테고리 3 제외 + 정렬 적용 목록 조회
        return productDAO.findAllSortedExcludingCategory(3L, sort)
                .stream()
                // 4) 엔티티 → DTO 매핑
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 카테고리 한정 정렬 조회. (PROD-004 + PROD-006)
     * 허용값: priceAsc | priceDesc | latest, null/빈값은 latest.
     */
    public List<ProductDTO> getProductsByCategorySorted(Long categoryId, String sortType) {
        String key = (sortType == null || sortType.isBlank()) ? "latest" : sortType.trim();
        Sort sort = switch (key) {
            case "priceAsc"  -> Sort.by(Sort.Order.asc("price"),  Sort.Order.asc("id"));
            case "priceDesc" -> Sort.by(Sort.Order.desc("price"), Sort.Order.asc("id"));
            default          -> Sort.by(Sort.Order.desc("registrationDate"), Sort.Order.desc("id"));
        };
        return productDAO.findByCategorySorted(categoryId, sort)
                .stream().map(this::convertToDTO).toList();
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