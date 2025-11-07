package com.project.shift.product.dao;

import com.project.shift.product.entity.Category;
import java.util.List;

/**
 * 카테고리 관련 데이터 접근 객체(DAO) 인터페이스입니다.
 * - 데이터베이스에서 카테고리 정보를 조회하고, 관리하는 메소드들을 정의합니다.
 */
public interface ICategoryDAO {

    /**
     * 모든 카테고리 목록을 조회하는 메소드입니다.	(PROD-003)
     * 
     * @return 카테고리 목록
     */
    List<Category> findAll();

    /**
     * 카테고리 ID로 카테고리 정보를 조회하는 메소드입니다.	(PROD-004)
     * 
     * @param categoryId 카테고리 ID
     * @return 카테고리 정보
     */
    Category findById(Long categoryId);
}