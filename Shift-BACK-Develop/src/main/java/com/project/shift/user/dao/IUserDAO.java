package com.project.shift.user.dao;

import java.util.Optional;

import com.project.shift.user.entity.UserEntity;

public interface IUserDAO {
    // ID 중복 체크
    boolean existsByLoginId(String loginId);
    // 연락처 중복 체크
    boolean existsByPhone(String phone);
    // 리뷰 
    Optional<UserEntity> findById(Long userId);

    Optional<UserEntity> findByNameAndPhone(String name, String phone);
}