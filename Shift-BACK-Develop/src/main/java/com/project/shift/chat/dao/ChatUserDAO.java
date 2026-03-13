package com.project.shift.chat.dao;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.project.shift.chat.entity.ChatUserEntity;
import com.project.shift.chat.repository.ChatUserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChatUserDAO {

	private final ChatUserRepository chatUserRepo;

	public ChatUserEntity getUserInfoByPhone(String phone) {
		return chatUserRepo.findByPhoneFlexible(phone);
	}
	
	// 채팅 - 마이페이지 정보 반환
	public Optional<ChatUserEntity> getChatUserInfo(long userId) {
		return chatUserRepo.findById(userId);
	}
	
}
