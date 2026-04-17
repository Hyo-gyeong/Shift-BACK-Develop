package com.project.shift.auth.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.shift.auth.dao.AuthDAO;
import com.project.shift.auth.dto.LoginRequestDTO;
import com.project.shift.auth.dto.LoginResponseDTO;
import com.project.shift.auth.entity.RefreshTokenEntity;
import com.project.shift.auth.repository.RefreshTokenRepository;
import com.project.shift.global.jwt.JwtService;
import com.project.shift.user.entity.UserEntity;

import lombok.extern.slf4j.Slf4j;

// ================================================================
// [REFACTOR 2026-04-14] 항목 3: AuthService의 RefreshToken 저장소 분리
//   변경 요약:
//     1) foundUser.setRefreshToken() + authDao.updateUser() 패턴 제거
//        -> RefreshTokenRepository.save()로 단독 테이블만 갱신
//     2) foundUser.getRefreshToken() 대신 RefreshTokenRepository.findById() 사용
//     3) 로그아웃 시 authDao.updateRefreshToken() 대신
//        RefreshTokenRepository.deleteById() 사용
//
//   성능 및 구조적 효과:
//     - USERS 테이블의 Row Lock 경합 제거
//       (로그인/리프레시 시 USERS UPDATE가 완전히 사라짐)
//     - 유저 정보 조회 쿼리(/users/me, /users/points 등)와 토큰 갱신이
//       서로 다른 물리 Row를 건드리므로 동시성 향상
//     - JwtService의 refreshToken 만료 시각을 엔티티에 명시적으로 저장하여
//       "토큰 값 일치 여부 + 만료 여부" 이중 검증 가능 (JWT 자체 만료와 독립)
//     - 추후 Redis 이관 시 이 Service 내부만 Repository 교체로 처리 가능
// ================================================================
@Slf4j
@Service
public class AuthService {

    private final AuthDAO authDao;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthService(AuthDAO authDao,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager,
                       RefreshTokenRepository refreshTokenRepository) {
        this.authDao = authDao;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    // 로그인
    @Transactional
    public LoginResponseDTO login(LoginRequestDTO loginInfo) {
        // 입력값 검증 수행
        UsernamePasswordAuthenticationToken cred = new UsernamePasswordAuthenticationToken(
                loginInfo.loginId(),
                loginInfo.password()
        );

        Authentication authentication = authenticationManager.authenticate(cred);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // dto -> entity로 변환
        UserEntity userEntity = UserEntity.builder()
                .loginId(loginInfo.loginId())
                .build();
        UserEntity foundUser = authDao.getUser(userEntity);

        Long userId = foundUser.getUserId();
        String name = foundUser.getName();

        log.info("[AUTH] 인증 성공, 토큰 발급 및 리프레시 토큰 갱신 시작 UserId: {}", userId);

        String accessToken = jwtService.createAccessToken(userId, name);
        String refreshToken = jwtService.createRefreshToken(userId);

        // ================================================================
        // [REFACTOR 2026-04-14] 항목 3: USERS 업데이트 -> REFRESH_TOKENS upsert
        //   변경 전:
        //     foundUser.setRefreshToken(refreshToken);
        //     authDao.updateUser(foundUser);
        //   변경 후: saveRefreshToken() 헬퍼 메서드 추출
        // ================================================================
        saveRefreshToken(foundUser, refreshToken);

        log.info("[AUTH] 리프레시 토큰 갱신 완료 UserId: {}", userId);

        return new LoginResponseDTO(accessToken, refreshToken);
    }

    @Transactional
    public void logout() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getName());

        log.info("[AUTH] 로그아웃 시작 UserId: {}", userId);

        // ================================================================
        // [REFACTOR 2026-04-14] 항목 3: USERS.REFRESH_TOKEN NULL 업데이트 ->
        //                               REFRESH_TOKENS 행 삭제
        //   변경 전: authDao.updateRefreshToken(userId)
        //            (JPQL UPDATE u SET u.refreshToken = NULL ...)
        //   변경 후: refreshTokenRepository.deleteById(userId)
        //
        //   존재하지 않아도(이미 로그아웃된 상태) 조용히 통과되도록
        //   existsById 체크 후 삭제.
        // ================================================================
        if (refreshTokenRepository.existsById(userId)) {
            refreshTokenRepository.deleteById(userId);
        }

        log.info("[AUTH] 로그아웃 완료 UserId: {}", userId);
    }

    // 토큰 재발급
    @Transactional
    public LoginResponseDTO refresh(String accessToken, String refreshToken) {
        // refresh token 검증
        validateRefreshToken(refreshToken);

        // 토큰의 값(userId)이 서로 일치하는지 체크
        Long userId = validateTokenPair(accessToken, refreshToken);

        // DB의 정보와 같은지 체크
        UserEntity foundUser = validateUserByToken(userId, refreshToken);

        // 토큰 재발급 실행
        String newAccessToken = jwtService.createAccessToken(foundUser.getUserId(), foundUser.getName());
        String newRefreshToken = jwtService.createRefreshToken(foundUser.getUserId());

        // ================================================================
        // [REFACTOR 2026-04-14] 항목 3: USERS 업데이트 -> REFRESH_TOKENS 갱신
        //   변경 전:
        //     foundUser.setRefreshToken(newRefreshToken);
        //     authDao.updateUser(foundUser);
        //   변경 후: saveRefreshToken() 헬퍼 재사용
        // ================================================================
        saveRefreshToken(foundUser, newRefreshToken);

        return new LoginResponseDTO(newAccessToken, newRefreshToken);
    }

    private void validateRefreshToken(String refreshToken) {
        // 토큰 유효성 체크
        if (!jwtService.isValidToken(refreshToken)) {
            throw new BadCredentialsException("[SYSTEM] 유효하지 않은 리프레시 토큰입니다.");
        }
        // 토큰 타입이 refresh 인지 체크
        if (!jwtService.isRefreshToken(refreshToken)) {
            throw new BadCredentialsException("[SYSTEM] 토큰 타입이 리프레시 토큰이 아닙니다.");
        }
    }

    private Long validateTokenPair(String accessToken, String refreshToken) {
        Long userIdFromAccess = jwtService.extractUserIdFromExpiredValidToken(accessToken);
        if (userIdFromAccess == null) {
            throw new BadCredentialsException("[SYSTEM] 신뢰할 수 없는 엑세스 토큰입니다.");
        }

        Long userIdFromRefresh = jwtService.extractUserIdFromValidToken(refreshToken);

        // 두 토큰의 짝이 맞는지 체크
        if (!userIdFromAccess.equals(userIdFromRefresh)) {
            throw new BadCredentialsException("[SYSTEM] 토큰이 서로 일치하지 않습니다.");
        }
        return userIdFromRefresh;
    }

    // ================================================================
    // [REFACTOR 2026-04-14] 항목 3: 토큰 검증 로직을 REFRESH_TOKENS 기준으로 변경
    //   변경 전:
    //     foundUser = authDao.getUserById(userId);
    //     foundUser.getRefreshToken()과 파라미터 비교
    //   변경 후:
    //     1) RefreshTokenRepository에서 토큰 Row 조회
    //     2) 없으면 인증 실패 (로그아웃/탈퇴 상태)
    //     3) 값이 파라미터와 다르면 인증 실패
    //     4) UserEntity는 AccessToken 재발급 시 필요한 name/userId 때문에
    //        별도 authDao.getUserById로 한 번만 조회
    // ================================================================
    private UserEntity validateUserByToken(Long userId, String refreshToken) {
        Optional<RefreshTokenEntity> tokenOpt = refreshTokenRepository.findById(userId);

        if (tokenOpt.isEmpty() || !refreshToken.equals(tokenOpt.get().getTokenValue())) {
            throw new BadCredentialsException("[SYSTEM] 리프레시 토큰이 저장된 리프레시 토큰과 일치하지 않습니다.");
        }

        UserEntity foundUser = authDao.getUserById(userId);
        if (foundUser == null) {
            throw new BadCredentialsException("[SYSTEM] 사용자를 찾을 수 없습니다.");
        }

        return foundUser;
    }

    // ================================================================
    // [REFACTOR 2026-04-14] 항목 3: RefreshToken 저장/갱신 헬퍼
    //   upsert 전략:
    //     - @MapsId 공유 PK 특성상 findById(userId)로 존재 여부 확인 후
    //       있으면 내부 상태 변경(refreshToken), 없으면 신규 Builder 생성
    //     - save() 호출은 Hibernate가 알아서 INSERT/UPDATE 결정
    //
    //   expiredAt:
    //     - JwtService.getRefreshTokenValidity()로 실제 설정값을 읽어 DB와 동기화.
    // ================================================================
    private void saveRefreshToken(UserEntity foundUser, String newTokenValue) {
        LocalDateTime newExpiredAt = LocalDateTime.now().plus(jwtService.getRefreshTokenValidity());

        Optional<RefreshTokenEntity> existingOpt =
                refreshTokenRepository.findById(foundUser.getUserId());

        RefreshTokenEntity entity = existingOpt
                .map(e -> {
                    e.refreshToken(newTokenValue, newExpiredAt);
                    return e;
                })
                .orElseGet(() -> RefreshTokenEntity.builder()
                        .user(foundUser)
                        .tokenValue(newTokenValue)
                        .expiredAt(newExpiredAt)
                        .build());

        refreshTokenRepository.save(entity);
    }
}
