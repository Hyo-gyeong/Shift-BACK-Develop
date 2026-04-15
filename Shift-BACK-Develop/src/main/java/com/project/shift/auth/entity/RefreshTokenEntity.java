package com.project.shift.auth.entity;

import java.time.LocalDateTime;

import com.project.shift.user.entity.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// ================================================================
// [REFACTOR 2026-04-14] 항목 3: RefreshToken 엔티티 분리 신규 생성
//   배경:
//     기존 UserEntity에 refreshToken 필드가 포함되어 있어,
//     토큰 갱신 시마다 USERS 테이블의 유저 Row 전체에 Row Lock이 걸리고
//     유저 정보 조회와 경합(contention)을 일으킴
//
//   설계 결정: 공유 PK(@MapsId)
//     - USERS 1 : REFRESH_TOKENS 1 의 진정한 1:1 관계를 구조적으로 보장
//     - 별도 시퀀스 불필요 (User의 PK를 그대로 재사용)
//     - FK 역할도 PK가 겸임하여 인덱스 1개만 필요
//     - 사용자가 제시한 별도 ID 방식은 UNIQUE 제약으로 1:1을 이중 표현하는
//       형태라 @MapsId 방식이 도메인 의미를 더 정직하게 반영
//
//   효과:
//     1) Row Lock 경합 해소 — AuthService의 토큰 갱신이 USERS를 건드리지 않음
//     2) 데이터 생명주기 분리 — 휘발성 토큰을 영구성 유저와 분리
//     3) SRP 준수 — UserEntity는 회원 정보, RefreshTokenEntity는 인증만 담당
//     4) Redis 이관 용이 — 이 엔티티만 교체하면 도메인 코드 변경 불필요
// ================================================================
@Entity
@Table(name = "REFRESH_TOKENS")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshTokenEntity {

    // @MapsId 공유 PK 전략: User의 userId를 이 엔티티의 PK로 재사용
	// 외래 키(FK)를 기본 키(PK)로 동시에 사용(식별 관계)할 때 연관관계 매핑과 식별자 매핑을 연결하는 핵심 어노테이션
    @Id
    @Column(name = "USER_ID")
    private Long userId;

    @MapsId  // user.userId 값을 @Id 필드(userId)로 자동 매핑
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private UserEntity user;

    @Column(name = "TOKEN_VALUE", nullable = false, length = 500)
    private String tokenValue;

    @Column(name = "EXPIRED_AT", nullable = false)
    private LocalDateTime expiredAt;

    @Builder
    RefreshTokenEntity(UserEntity user, String tokenValue, LocalDateTime expiredAt) {
        this.user = user;
        this.tokenValue = tokenValue;
        this.expiredAt = expiredAt;
    }

    // 토큰 갱신 (값 객체 치환이 아닌 내부 상태 변경)
    public void refreshToken(String newToken, LocalDateTime newExpiredAt) {
        this.tokenValue = newToken;
        this.expiredAt = newExpiredAt;
    }
}
