package com.project.shift.auth.dao;

import com.project.shift.auth.repository.AuthRepository;
import com.project.shift.user.entity.UserEntity;
import org.springframework.stereotype.Repository;

@Repository
public class AuthDAO implements IAuthDAO{

    private final AuthRepository authRepository;

    public AuthDAO(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public UserEntity getUser(UserEntity userEntity) {
        return authRepository.findByLoginId(userEntity.getLoginId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    @Override
    public UserEntity getUserById(Long userId) {
        return authRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }


    @Override
    public void updateUser(UserEntity userEntity) {
        authRepository.save(userEntity);
    }
}
