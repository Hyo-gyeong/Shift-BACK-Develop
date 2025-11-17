package com.project.shift.chat.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.project.shift.chat.entity.ChatUserEntity;
import com.project.shift.chat.repository.ChatUserRepository;
import com.project.shift.chat.repository.ChatroomRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChatUserDAO {

	private final ChatUserRepository chatUserRepo;

	public ChatUserEntity getUserInfo(int id) {
		return chatUserRepo.findByUserId(id);
	}
	
	public List<ChatUserEntity> getUserInfoByIds(List<Integer> userIds) {
		return chatUserRepo.findUserInfoByIds(userIds);
	}

	public ChatUserEntity getUserInfoByPhone(String phone) {
		return chatUserRepo.findByPhoneFlexible(phone);
	}
	
}
