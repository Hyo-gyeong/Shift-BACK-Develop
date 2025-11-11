package com.project.shift.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.project.shift.product.entity.Image;
import org.springframework.stereotype.Repository;

/**
 * 금액권(category_id=3) 대표 이미지 조회용 Repository
 */
@Repository
public interface PointRepository extends JpaRepository<Image, Long> {

    /**
     * category_id=3 금액권의 대표 이미지 URL 조회
     */
    @Query("SELECT i.imageUrl FROM Image i WHERE i.product.category.categoryId = 3 AND i.isRepresentative = 'Y'")
    String findPointImage();
}