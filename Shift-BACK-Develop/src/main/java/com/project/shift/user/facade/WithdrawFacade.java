package com.project.shift.user.facade;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.shift.shop.service.ICartService;
import com.project.shift.shop.service.IOrderService;
import com.project.shift.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//회원 탈퇴 오케스트레이션 Facade
//여러 도메인 Service를 조합하는 역할만 담당
//SecurityContextHolder 등 Security 인프라 처리는 Controller 책임
@Slf4j
@Service
@RequiredArgsConstructor
public class WithdrawFacade {

    private final UserService userService;
    private final IOrderService orderService;
    private final ICartService cartService;

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
        
        // 4. 친구 관계 삭제 — chat 패키지 리팩토링 완료 후 추가
        // friendService.deleteAllFriends(userId);

        // 5. 채팅방 유저 상태 처리 — chat 패키지 리팩토링 완료 후 추가
        // chatroomUserService.withdrawUser(userId);

        // 6. 유저 논리 삭제 (항상 마지막)
        userService.markAsWithdrawn(userId);
        
        log.info("[USER] 회원 탈퇴 완료 userId={}", userId);
    }
}