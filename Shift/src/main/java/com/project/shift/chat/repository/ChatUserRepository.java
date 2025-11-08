package com.project.shift.chat.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.shift.chat.entity.ChatUserEntity;
import com.project.shift.user.entity.UserEntity;

public interface ChatUserRepository extends JpaRepository<ChatUserEntity, Long>{
	ChatUserEntity findByUserId(int userId);
}
