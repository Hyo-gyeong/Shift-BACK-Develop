package com.project.shift.user.facade;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.shift.auth.repository.RefreshTokenRepository;
import com.project.shift.shop.service.ICartService;
import com.project.shift.shop.service.IOrderService;
import com.project.shift.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//회원 탈퇴 오케스트레이션 Facade
//여러 도메인 Service를 조합하는 역할만 담당
//SecurityContextHolder 등 Security 인프라 처리는 Controller 책임
// ================================================================
// [REFACTOR 2026-04-14] 항목 3: RefreshToken 삭제 단계 추가
//   배경:
//     - refreshToken이 UserEntity에서 REFRESH_TOKENS 테이블로 분리됨
//     - UserEntity.withdraw()는 USERS에 대한 논리 삭제(Soft Delete)만 수행
//     - REFRESH_TOKENS는 물리 삭제 대상 (휘발성 데이터)
//
//   처리 위치:
//     - 도메인 서비스가 아닌 Facade에서 담당
//     - 이유: 탈퇴 오케스트레이션 흐름의 일부이므로 Facade의 책임 범위
//     - UserService에 넣으면 유저 도메인이 인증 토큰을 직접 의존하게 됨
//
//   순서:
//     - 배송 중 검증 실패 시 토큰이 살아있어야 하므로,
//       반드시 hasActiveDeliveries 검증 이후에 배치
// ================================================================
@Slf4j
@Service
@RequiredArgsConstructor
public class WithdrawFacade {

    private final UserService userService;
    private final IOrderService orderService;
    private final ICartService cartService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void withdraw(Long userId) {
        log.info("[USER] 회원 탈퇴 시작 userId={}", userId);

        // 1. 결제 미완료(P) 주문 자동 삭제
        orderService.cancelPendingOrders(userId);

        // 2. 배송 중인(S) 주문 존재 여부 확인
        if (orderService.hasActiveDeliveries(userId)) {
            throw new IllegalStateException(
                "현재 배송 중인 상품이 있어 탈퇴할 수 없습니다.\n" +
                "상품이 도착하여 구매 확정된 후 다시 시도해주세요."
            );
        }

        // 3. 장바구니 비우기
        cartService.clearCartByUserId(userId);

        // 4. [REFACTOR 2026-04-14] 리프레시 토큰 물리 삭제
        //    존재하지 않아도 조용히 통과 (이미 로그아웃된 경우)
        if (refreshTokenRepository.existsById(userId)) {
            refreshTokenRepository.deleteById(userId);
        }

        // 5. 친구 관계 삭제 — chat 패키지 리팩토링 완료 후 추가
        // friendService.deleteAllFriends(userId);

        // 6. 채팅방 유저 상태 처리 — chat 패키지 리팩토링 완료 후 추가
        // chatroomUserService.withdrawUser(userId);

        // 7. 유저 논리 삭제 (항상 마지막)
        userService.markAsWithdrawn(userId);

        log.info("[USER] 회원 탈퇴 완료 userId={}", userId);
    }
}