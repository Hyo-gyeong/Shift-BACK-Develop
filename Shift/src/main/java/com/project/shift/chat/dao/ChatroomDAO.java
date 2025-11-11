package com.project.shift.chat.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.project.shift.chat.entity.ChatroomEntity;
import com.project.shift.chat.repository.ChatroomRepository;

@Component
public class ChatroomDAO {

	@Autowired
	ChatroomRepository chatroomRepo;

	public List<ChatroomEntity> getUserChatrooms(int userId) {
		return chatroomRepo.findByFromUserId(userId);
	}
	
	public List<Integer> findChatroomIdsForUsers(int fromId, int toId) {
		return chatroomRepo.findChatroomIdsForUsers(fromId, toId);
	}
	
}
