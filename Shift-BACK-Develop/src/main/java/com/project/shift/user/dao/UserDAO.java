package com.project.shift.user.dao;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.project.shift.user.entity.UserEntity;
import com.project.shift.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDAO implements IUserDAO {

    private final UserRepository userRepository;
    
    @Override
    public boolean existsByLoginId(String loginId) {
        return userRepository.existsByLoginId(loginId);
    }

    @Override
    public boolean existsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    // 사용자 ID로 조회 (리뷰 작성자 이름 확인용)
    @Override
    public Optional<UserEntity> findById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<UserEntity> findByNameAndPhone(String name, String phone) {
        return userRepository.findByNameAndPhone(name, phone);
    }
}
