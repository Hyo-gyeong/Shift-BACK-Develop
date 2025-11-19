package com.project.shift.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.shift.chat.entity.ChatroomUserEntity;

public interface ChatroomUserRepository extends JpaRepository<ChatroomUserEntity, Long>{

}
