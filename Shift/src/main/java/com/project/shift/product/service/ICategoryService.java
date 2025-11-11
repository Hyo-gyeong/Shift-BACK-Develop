package com.project.shift.product.service;

import com.project.shift.product.dto.CategoryDTO;
import java.util.List;

/**
 * [I-SERVICE-004] 카테고리 관련 서비스 인터페이스
 * ---------------------------------------------------------
 * - PROD-003 : 카테고리 목록 조회
 */
public interface ICategoryService {

    /** [PROD-003] 카테고리 목록 조회 */
    List<CategoryDTO> getCategoryList();
}