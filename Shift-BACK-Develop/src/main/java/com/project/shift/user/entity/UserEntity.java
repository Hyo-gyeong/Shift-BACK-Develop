package com.project.shift.user.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.SQLRestriction;

import com.project.shift.user.UserConstants;

// 가독성이 떨어지고 클래스 충돌 가능성이 있고 유지보수가 어렵기 때문에 하나 씩 import
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "USERS")
@SQLRestriction("DELETED_AT IS NULL") //모든 SQL 쿼리에 공통된 WHERE 조건을 추가, DELETED_AT이 NULL인 값만 조회
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(
    		strategy = GenerationType.SEQUENCE,
    		generator = "SEQ_USERS_GENERATOR"
    )
    @SequenceGenerator( // 필드 레벨에 작성하는 것을 권장
            name = "SEQ_USERS_GENERATOR",
            sequenceName = "seq_users",
            allocationSize = 1
    )
    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "LOGIN_ID")
    private String loginId;

    private String password;

    private String name;

    private String phone;

    private String address;

    private Integer points; // default 0

    @Setter
    @Column(name = "REFRESH_TOKEN")
    private String refreshToken;

    @Column(
            name = "ADMIN_FLAG",
            columnDefinition = "CHAR(1)"
    )
    private String adminFlag; // default 'N'

    @Column(name = "DELETED_AT")
    private LocalDateTime deletedAt;

    //수정 가능 필드만 메서드로 제공
    public void updateInfo(String name, String phone, String address) {
        this.name = name;
        this.phone = phone;
        this.address = address;
    }
    
    // 논리적 탈퇴
    public void withdraw() {
        this.loginId = UserConstants.DELETED_USER_PREFIX + this.getUserId();
        this.password = UserConstants.DELETED_USER_PASSWORD;
        this.name = UserConstants.DELETED_USER;
        this.phone = null;
        this.address = null;
        this.points = 0;
        this.refreshToken = null;
        this.deletedAt = LocalDateTime.now();
	}
}