package com.project.shift.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.shift.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String>{

}
