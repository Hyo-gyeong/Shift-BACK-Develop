package com.project.shift.user.dao;

import com.project.shift.product.entity.Review;
import com.project.shift.user.entity.UserEntity;
import com.project.shift.user.repository.UserRepository;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

@Service
public class UserDAO implements IUserDAO {

    private final UserRepository userRepository;

    public UserDAO(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    @Override
    public boolean existsByLoginId(String loginId) {
        return userRepository.existsByLoginId(loginId);
    }

    @Override
    public boolean existsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    /**
     * 사용자 ID로 조회 (리뷰 작성자 이름 확인용)
     */
    @Override
    public Optional<UserEntity> findById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<UserEntity> findByNameAndPhone(String name, String phone) {
        return userRepository.findByNameAndPhone(name, phone);
    }
}
