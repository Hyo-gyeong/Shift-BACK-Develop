package com.project.shift.user.dao;

import com.project.shift.user.entity.UserEntity;
import com.project.shift.user.repository.UserRepository;
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
}
