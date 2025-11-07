package com.project.shift.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.shift.chat.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long>{
	
}
