package com.project.shift.product.dao;

import com.project.shift.product.entity.Image;
import com.project.shift.product.repository.ImageRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 이미지 DAO
 * - EntityManager를 사용해 이미지 데이터 직접 조작
 */
@Repository
public class ImageDAO implements IImageDAO {

    @PersistenceContext
    private EntityManager entityManager;

    private final ImageRepository imageRepository;

    public ImageDAO(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    /** 특정 상품의 모든 이미지 조회 */
    @Override
    public List<Image> findByProductId(Long productId) {
        String query = "SELECT i FROM Image i WHERE i.product.id = :productId";
        return entityManager.createQuery(query, Image.class)
                            .setParameter("productId", productId)
                            .getResultList();
    }

    /** 특정 상품의 대표 이미지 초기화 (전부 N으로 변경) */
    @Transactional
    public void resetRepresentative(Long productId) {
        String query = "UPDATE Image i SET i.isRepresentative = 'N' WHERE i.product.id = :productId";
        entityManager.createQuery(query)
                     .setParameter("productId", productId)
                     .executeUpdate();
    }

    /** 특정 이미지의 대표 여부 설정 */
    @Transactional
    public void updateRepresentative(Long imageId, String value) {
        String query = "UPDATE Image i SET i.isRepresentative = :value WHERE i.id = :imageId";
        entityManager.createQuery(query)
                     .setParameter("value", value)
                     .setParameter("imageId", imageId)
                     .executeUpdate();
    }
}