package com.project.shift.auth.dao;

import com.project.shift.user.entity.UserEntity;

public interface IAuthDAO {

    UserEntity getUser(UserEntity userEntity);

    UserEntity getUserById(Long userId);

    void updateUser(UserEntity userEntity);
}
