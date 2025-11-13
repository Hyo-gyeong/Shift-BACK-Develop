package com.project.shift.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "USERS")
@SequenceGenerator(
        name = "SEQ_USERS_GENERATOR",
        sequenceName = "seq_users",
        allocationSize = 1
)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USERS_GENERATOR")
    @Column(name = "USER_ID", nullable = false, updatable = false)
    private Long userId;

    @Column(name = "LOGIN_ID", nullable = false, unique = true)
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

    @Column(name = "REFRESH_TOKEN")
    private String refreshToken;

    @Column(
        name = "ADMIN_FLAG",
        nullable = false,
        columnDefinition = "CHAR(1)"
    )
    private String adminFlag = "N";
}