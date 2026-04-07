package com.project.shift.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.shift.user.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByLoginId(String loginId);

    boolean existsByPhone(String phone);

    // 로그인 아이디로 특정 사용자 추출
    Optional<UserEntity> findByLoginId(String loginId);

    // 사용자 이름과 연락처로 특정 사용자 추출
    Optional<UserEntity> findByNameAndPhone(String name, String phone);
}