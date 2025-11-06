package com.project.shift.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@SequenceGenerator(
        name = "SEQ_USERS_GENERATOR",
        sequenceName = "seq_users",
        allocationSize = 1
)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // jpa용 기본 생성자 (외부 직접생성 제한)
@AllArgsConstructor
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USERS_GENERATOR")
    @Column(name = "user_id", nullable = false, updatable = false)
    private Long userId;

    @Column(name = "login_id", nullable = false, unique = true)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column
    private String address;

    @Column
    private Integer points = 0;

    @Column(name = "admin_flag", length = 1)
    private String adminFlag = "N";

    @Column
    private String refreshToken;
}
