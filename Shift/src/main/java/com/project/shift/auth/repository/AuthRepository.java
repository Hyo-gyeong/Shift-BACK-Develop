package com.project.shift.auth.repository;

import com.project.shift.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByLoginId(String loginId);

    Optional<UserEntity> findByUserId(Long userId);

    Optional<UserEntity> findByRefreshToken(String refreshToken);

    @Modifying
    @Query(value = "UPDATE UserEntity u SET u.refreshToken = NULL WHERE u.userId = :userId")
    void updateRefreshTokenById(Long userId);

}
