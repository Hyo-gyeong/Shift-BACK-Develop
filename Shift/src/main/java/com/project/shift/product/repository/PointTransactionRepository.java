package com.project.shift.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.shift.product.entity.PointTransaction;

public interface PointTransactionRepository extends JpaRepository<PointTransaction, Long> {

    List<PointTransaction> findByUserIdOrderByCreatedAtDesc(Long userId);
}
