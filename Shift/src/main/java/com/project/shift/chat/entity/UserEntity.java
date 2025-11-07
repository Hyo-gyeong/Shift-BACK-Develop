package com.project.shift.chat.entity;

import com.project.shift.chat.dto.UserDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="Users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {
	
    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "seq_users"
    )
    @SequenceGenerator(
        name = "seq_users",
        sequenceName = "seq_users",
        allocationSize = 1
    )
    @Column(name = "user_id")
    private long userId;

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
    private int points; // DEFAULT 0

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "admin_flag", length = 1)
    private String adminFlag; // DEFAULT 'N', 'Y' 또는 'N'
    
    // DTO -> Entity 변환
    public static UserEntity toEntity(UserDTO dto) {
        return UserEntity.builder()
                .userId(dto.getUserId())
                .loginId(dto.getLoginId())
                .password(dto.getPassword())
                .name(dto.getName())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .points(dto.getPoints())
                .refreshToken(dto.getRefreshToken())
                .adminFlag(dto.getAdminFlag())
                .build();
    }
    
}
