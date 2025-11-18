package com.project.shift.user.dao;

import java.util.Optional;
import java.util.stream.Stream;

import com.project.shift.product.entity.Review;
import com.project.shift.user.entity.UserEntity;

public interface IUserDAO {
    // 저장
    UserEntity save(UserEntity userEntity);
    // ID 중복 체크
    boolean existsByLoginId(String loginId);
    // 연락처 중복 체크
    boolean existsByPhone(String phone);
    // 리뷰 
    Optional<UserEntity> findById(Long userId);

    Optional<UserEntity> findByNameAndPhone(String name, String phone);
}