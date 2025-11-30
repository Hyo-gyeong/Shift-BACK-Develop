package com.project.shift.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.shift.product.entity.NewReviewEntity;

public interface ReviewEntityRepository extends JpaRepository<NewReviewEntity, Long> {

}