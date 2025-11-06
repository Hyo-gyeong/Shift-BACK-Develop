package com.project.shift.user.service;

import com.project.shift.user.entity.UserEntity;
import com.project.shift.user.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.security.core.userdetails.User.UserBuilder;

// Spring Security 인증 과정에서 자동으로 호출되는 메서드

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        Optional<UserEntity> user = userRepository.findByLoginId(loginId);

        UserBuilder builder;

        if (user.isPresent()) {
            UserEntity currentUser = user.get();
            builder = User.withUsername(loginId);
            builder.password(currentUser.getPassword());

            // adminFlag가 'Y' 이면 ADMIN, 'N' 이면 USER
            String role = "Y".equalsIgnoreCase(currentUser.getAdminFlag()) ? "ADMIN" : "USER";
            builder.roles(role);
        } else {
            throw new UsernameNotFoundException("User not found"); // 해당 loginId 가 DB에 없으면 예외
        }
        return builder.build();
    }
}
