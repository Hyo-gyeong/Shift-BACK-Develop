package com.project.shift.chat.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "Users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "login_id", unique = true)
    private String loginId;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "phone", unique = true)
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "points")
    private Integer points; // DEFAULT 0

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "admin_flag", length = 1)
    private String adminFlag; // DEFAULT 'N', 'Y' 또는 'N'
}
