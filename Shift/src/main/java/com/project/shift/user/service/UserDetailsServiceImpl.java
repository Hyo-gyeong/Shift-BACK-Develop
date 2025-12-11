package com.project.shift.user.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.shift.user.entity.UserEntity;
import com.project.shift.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// Spring Security 인증 과정에서 자동으로 호출되는 메서드

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        log.info("[AUTH] UserDetailsServiceImpl: 사용자 조회 시도 LoginId {}", loginId);
        Optional<UserEntity> user = userRepository.findByLoginId(loginId);

        UserBuilder builder;

        if (user.isPresent()) {
            UserEntity currentUser = user.get();
            builder = User.withUsername(loginId);
            builder.password(currentUser.getPassword());

            log.info("[AUTH] UserDetailsServiceImpl: 사용자 찾음 역할 LoginId {}", loginId);

            // adminFlag가 'Y' 이면 ADMIN, 'N' 이면 USER
            String role = "Y".equalsIgnoreCase(currentUser.getAdminFlag()) ? "ADMIN" : "USER";
            builder.roles(role);
        } else {
            log.warn("[AUTH] UserDetailsServiceImpl: 사용자를 찾지 못함 LoginId {}", loginId);
            throw new UsernameNotFoundException("User not found"); // 해당 loginId 가 DB에 없으면 예외
        }
        return builder.build();
    }
}
