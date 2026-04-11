package com.project.shift.user;

public final class UserConstants {
	// 객체 생성 방지 (인스턴스화 방지)
    private UserConstants() {}

    // 탈퇴 처리 시 loginId에 붙는 prefix
    // isLoginIdAvailable()의 차단 로직("deleted"로 시작)과 반드시 일치해야 함
    public static final String DELETED_USER_ID_PREFIX = "deleted_id_";
    public static final String DELETED_USER_PW_PREFIX = "deleted_pw";
}