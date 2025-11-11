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
	
	public ChatroomEntity insertChatroom(ChatroomEntity entity) {
	    // 이미 존재하는 채팅방은 저장(덮어쓰기)불가
	    if (chatroomRepo.existsById(entity.getChatroomId())) {
	        throw new IllegalStateException("Chatroom already exists with ID: " + entity.getChatroomId());
	    }
	    // 채팅방 생성
	    return chatroomRepo.save(entity);
	}
	
	public boolean deleteById(long chatroomId) {
		if (chatroomRepo.existsById(chatroomId)) {
	        chatroomRepo.deleteById(chatroomId);
	        return true;
	    }
	    return false;
	}
	
}
