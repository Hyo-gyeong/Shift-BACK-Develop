package com.project.shift.auth.dao;

import org.springframework.stereotype.Repository;

import com.project.shift.auth.repository.AuthRepository;
import com.project.shift.user.entity.UserEntity;

// ================================================================
// [REFACTOR 2026-04-14] 항목 3: updateRefreshToken 구현 제거
//   변경 전:
//     @Override
//     public void updateRefreshToken(Long userId) {
//         authRepository.updateRefreshTokenById(userId);
//     }
//   변경 후: 삭제 (IAuthDAO 인터페이스에서도 제거됨)
//     - AuthRepository.updateRefreshTokenById 자체가 사라졌기 때문
//     - AuthService는 RefreshTokenRepository를 직접 주입받아 사용
// ================================================================
@Repository
public class AuthDAO implements IAuthDAO{

    private final AuthRepository authRepository;

    public AuthDAO(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public UserEntity getUser(UserEntity userEntity) {
        return authRepository.findByLoginId(userEntity.getLoginId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    @Override
    public UserEntity getUserById(Long userId) {
        return authRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    @Override
    public void updateUser(UserEntity userEntity) {
        authRepository.save(userEntity);
    }
}
