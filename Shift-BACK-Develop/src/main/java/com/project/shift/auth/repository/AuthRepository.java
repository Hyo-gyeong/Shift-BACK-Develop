package com.project.shift.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.shift.user.entity.UserEntity;

// ================================================================
// [REFACTOR 2026-04-14] 항목 3: updateRefreshTokenById JPQL 제거
//   변경 전:
//     @Modifying
//     @Query("UPDATE UserEntity u SET u.refreshToken = NULL WHERE u.userId = :userId")
//     void updateRefreshTokenById(Long userId);
//
//   변경 후: 삭제
//     - refreshToken 필드가 UserEntity에서 사라졌으므로 JPQL 자체가 컴파일 불가
//     - 동일 역할은 RefreshTokenRepository.deleteById(userId)가 수행
//     - 연관 import (@Modifying, @Query)도 함께 제거
// ================================================================
@Repository
public interface AuthRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByLoginId(String loginId);

    Optional<UserEntity> findByUserId(Long userId);
}
