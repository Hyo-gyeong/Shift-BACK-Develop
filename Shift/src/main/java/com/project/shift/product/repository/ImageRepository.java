package com.project.shift.product.repository;

import com.project.shift.product.entity.Image;
import jakarta.persistence.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class ImageRepository {

    @PersistenceContext
    private EntityManager entityManager;

    // 상품에 속한 모든 이미지의 is_representative 값을 'N'으로 설정
    @Transactional
    public void updateIsRepresentativeByProductId(Long productId, String value) {
        String query = "UPDATE Image i SET i.isRepresentative = :value WHERE i.product.id = :productId";
        entityManager.createQuery(query)
                     .setParameter("value", value)
                     .setParameter("productId", productId)
                     .executeUpdate();
    }

    // 특정 이미지의 is_representative 값을 'Y'로 설정
    @Transactional
    public void updateIsRepresentativeByImageId(Long imageId, String value) {
        String query = "UPDATE Image i SET i.isRepresentative = :value WHERE i.id = :imageId";
        entityManager.createQuery(query)
                     .setParameter("value", value)
                     .setParameter("imageId", imageId)
                     .executeUpdate();
    }

    // 특정 상품에 속한 모든 이미지 조회
    public List<Image> findByProductId(Long productId) {
        String query = "SELECT i FROM Image i WHERE i.product.id = :productId";
        return entityManager.createQuery(query, Image.class)
                            .setParameter("productId", productId)
                            .getResultList();
    }
}