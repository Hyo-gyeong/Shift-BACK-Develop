package com.project.shift.product.service;

import com.project.shift.product.dto.CategoryDTO;

import java.util.List;

/**
 * 카테고리 관련 서비스 인터페이스입니다.
 * - 카테고리 목록을 조회하는 기능을 정의합니다.
 */
public interface ICategoryService {

    /**
     * 카테고리 목록을 조회하는 메소드입니다.
     * 
     * @return 카테고리 목록
     */
    List<CategoryDTO> getCategoryList();
}