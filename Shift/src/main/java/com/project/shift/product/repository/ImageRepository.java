package com.project.shift.product.repository;

import com.project.shift.product.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Image 엔티티에 대한 CRUD 작업을 처리하는 리포지토리.
 */
public interface ImageRepository extends JpaRepository<Image, Long> {

    /**
     * 특정 상품의 대표 이미지를 조회하는 메서드.
     * @param productId 상품 ID
     * @param isRepresentative 'Y' 또는 'N' 값으로 대표 이미지 여부 확인
     * @return 대표 이미지 (있을 경우 첫 번째 이미지)
     */
    Optional<Image> findFirstByProduct_IdAndIsRepresentative(Long productId, String isRepresentative);
}
