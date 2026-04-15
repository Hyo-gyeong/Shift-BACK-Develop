package com.project.shift.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.shift.auth.entity.RefreshTokenEntity;

// ================================================================
// [REFACTOR 2026-04-14] 항목 3: RefreshTokenRepository 신규 생성
//   역할:
//     AuthService의 로그인/리프레시/로그아웃 흐름에서 사용되는
//     REFRESH_TOKENS 전용 JPA 리포지토리.
//
//   쿼리 메서드 명명 규칙:
//     - RefreshTokenEntity가 @MapsId 공유 PK를 사용하므로,
//       @Id 필드(userId)를 통한 조회가 곧 기본 조회가 됨.
//     - 별도의 findByUser_UserId는 두지 않고 findById(userId) 를 그대로 활용.
//     - 삭제는 deleteById(userId) 로 단건 처리 (로그아웃 및 회원 탈퇴용)
//
//   JpaRepository<RefreshTokenEntity, Long> 선택 이유:
//     - PK 타입이 Long(userId)이므로 두 번째 타입 파라미터는 Long
//     - 기본 CRUD (save, findById, deleteById, existsById) 만으로 충분
// ================================================================
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    // findById는 JpaRepository 기본 제공이므로 별도 선언 불필요.
    // 명시적 의도 표현을 위해 userId 명명 오버로드만 추가한다.
    default Optional<RefreshTokenEntity> findByUserId(Long userId) {
        return findById(userId);
    }

    default void deleteByUserId(Long userId) {
        deleteById(userId);
    }
}
