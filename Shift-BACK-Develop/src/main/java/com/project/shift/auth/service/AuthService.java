package com.project.shift.auth.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.shift.auth.dto.request.LoginRequestDTO;
import com.project.shift.auth.dto.response.LoginResponseDTO;
import com.project.shift.auth.entity.RefreshTokenEntity;
import com.project.shift.auth.repository.RefreshTokenRepository;
import com.project.shift.global.jwt.JwtService;
import com.project.shift.user.entity.UserEntity;
import com.project.shift.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;

    // 로그인
    @Transactional
    public LoginResponseDTO login(LoginRequestDTO loginInfo) {
        // 입력값 검증 수행
        UsernamePasswordAuthenticationToken cred = new UsernamePasswordAuthenticationToken(
                loginInfo.loginId(),
                loginInfo.password()
        );

        // [리팩토링 2026-04-19] SecurityContextHolder 세팅 제거
        // 이전:
        //   SecurityContextHolder.getContext().setAuthentication(authentication);
        // 개선: Stateless JWT 환경에서 SecurityContextHolder 에 authentication 을 저장해도
        //       다음 요청은 새 스레드/새 컨텍스트에서 시작하므로 이 저장은 무의미한 no-op.
        //       인증 실패 시에는 authenticationManager 가 BadCredentialsException 을 던져
        //       GlobalExceptionHandler 가 401 응답으로 처리.
        authenticationManager.authenticate(cred);

        UserEntity foundUser = userRepository.findByLoginId(loginInfo.loginId())
                .orElseThrow(() -> new BadCredentialsException("[SYSTEM] 사용자를 찾을 수 없습니다."));

        Long userId = foundUser.getUserId();
        String name = foundUser.getName();

        log.info("[AUTH] 인증 성공, 토큰 발급 및 리프레시 토큰 갱신 시작 UserId: {}", userId);

        String accessToken = jwtService.createAccessToken(userId, name);
        String refreshToken = jwtService.createRefreshToken(userId);

        saveRefreshToken(foundUser, refreshToken);

        log.info("[AUTH] 리프레시 토큰 갱신 완료 UserId: {}", userId);

        return new LoginResponseDTO(accessToken, refreshToken);
    }

    // [리팩토링 2026-04-19] logout() 시그니처 변경 + SecurityContextHolder 의존 제거
    @Transactional
    public void logout(Long userId) {
        log.info("[AUTH] 로그아웃 시작 UserId: {}", userId);

        refreshTokenRepository.deleteById(userId);

        log.info("[AUTH] 로그아웃 완료 UserId: {}", userId);
    }

    // 토큰 재발급
    @Transactional
    public LoginResponseDTO refresh(String accessToken, String refreshToken) {
        // [리팩토링 2026-04-19] refresh() 검증 3단계 → 2단계로 단순화
        //   JwtService 만으로 가능한 서명/타입/짝 검증을 verifyRefreshPair 한 메서드로 통합.
        //   DB 기반 검증(validateUserByToken) 은 Repository 접근이 필요해 Service 에 유지.
        Long userId = jwtService.verifyRefreshPair(accessToken, refreshToken);

        // DB의 정보와 같은지 체크
        UserEntity foundUser = validateUserByToken(userId, refreshToken);

        // 토큰 재발급 실행
        String newAccessToken = jwtService.createAccessToken(foundUser.getUserId(), foundUser.getName());
        String newRefreshToken = jwtService.createRefreshToken(foundUser.getUserId());

        saveRefreshToken(foundUser, newRefreshToken);

        return new LoginResponseDTO(newAccessToken, newRefreshToken);
    }

    // [리팩토링 2026-04-19] validateRefreshToken / validateTokenPair 제거
    //       JwtService.verifyRefreshPair 로 통합하여 AuthService 의 refresh() 가 한 줄로 처리.
    private UserEntity validateUserByToken(Long userId, String refreshToken) {
        Optional<RefreshTokenEntity> tokenOpt = refreshTokenRepository.findById(userId);

        if (tokenOpt.isEmpty() || !refreshToken.equals(tokenOpt.get().getTokenValue())) {
            throw new BadCredentialsException("[SYSTEM] 리프레시 토큰이 저장된 리프레시 토큰과 일치하지 않습니다.");
        }

        UserEntity foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new BadCredentialsException("[SYSTEM] 사용자를 찾을 수 없습니다."));

        return foundUser;
    }

    private void saveRefreshToken(UserEntity foundUser, String newTokenValue) {
    	// expiredAt : 실제 설정값을 읽어 DB와 동기화 (7일)
        LocalDateTime newExpiredAt = LocalDateTime.now().plus(jwtService.getRefreshTokenValidity());

        Optional<RefreshTokenEntity> existingOpt =
                refreshTokenRepository.findById(foundUser.getUserId());

        RefreshTokenEntity entity = existingOpt
                .map(e -> {
                    e.refreshToken(newTokenValue, newExpiredAt);
                    return e;
                })
                .orElseGet(() -> RefreshTokenEntity.builder()
                        .userId(foundUser.getUserId())
                        .tokenValue(newTokenValue)
                        .expiredAt(newExpiredAt)
                        .build());

        refreshTokenRepository.save(entity);
    }
}
