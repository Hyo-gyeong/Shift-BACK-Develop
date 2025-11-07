package com.project.shift.product.repository;

import com.project.shift.product.entity.Category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 카테고리 데이터를 처리하는 JPA 리포지토리입니다.
 * - 데이터베이스에서 카테고리 정보를 조회하는 역할을 합니다.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // 모든 카테고리 정보를 조회하는 메소드 (JpaRepository가 기본적으로 제공)
    List<Category> findAll();
}