package com.project.shift.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.shift.product.entity.ReviewOriginEntity;

public interface ReviewEntityRepository extends JpaRepository<ReviewOriginEntity, Long> {

}