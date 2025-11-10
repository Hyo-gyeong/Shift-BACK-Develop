package com.project.shift.product.dao;

import com.project.shift.product.entity.Image;
import java.util.List;

/**
 * 이미지 DAO 인터페이스
 */
public interface IImageDAO {
    List<Image> findByProductId(Long productId);
}