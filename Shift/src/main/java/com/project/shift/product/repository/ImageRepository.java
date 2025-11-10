package com.project.shift.product.repository;

import com.project.shift.product.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    /**
     * 상품 ID 기준으로 이미지 목록 조회
     * Product 엔티티의 필드명은 id이므로 Product_Id로 지정해야 함.
     */
    List<Image> findByProduct_Id(Long productId);
}