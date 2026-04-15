package com.project.shift.auth.dao;

import com.project.shift.user.entity.UserEntity;

// ================================================================
// [REFACTOR 2026-04-14] 항목 3: updateRefreshToken 메서드 제거
//   변경 전:
//     void updateRefreshToken(Long userId);
//   변경 후: 삭제
//     - AuthService가 더 이상 이 DAO를 통해 리프레시 토큰을 다루지 않음
//     - 역할 이관처: RefreshTokenRepository.deleteById(userId)
//
//   updateUser는 그대로 유지:
//     향후 로그인 실패 카운트 등 USERS 자체 업데이트 시나리오를 위해 보존
// ================================================================
public interface IAuthDAO {

    UserEntity getUser(UserEntity userEntity);

    UserEntity getUserById(Long userId);

    void updateUser(UserEntity userEntity);
}
