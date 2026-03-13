package com.project.shift.product.repository;

import com.project.shift.product.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * [REP-002] 카테고리 Repository
 * ---------------------------------------------------------
 * - PROD-003 : 카테고리 목록 조회
 * - PROD-004 : 카테고리 상세 조회
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
}