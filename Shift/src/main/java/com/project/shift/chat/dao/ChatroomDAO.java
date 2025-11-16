package com.project.shift.chat.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.project.shift.chat.entity.ChatroomEntity;
import com.project.shift.chat.repository.ChatroomRepository;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor  // final 필드에 대한 생성자 자동 생성 (Autowired 대체)
public class ChatroomDAO {

	private final ChatroomRepository chatroomRepo;

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
		// 채팅방이 존재하면 삭제
		if (chatroomRepo.existsById(chatroomId)) {
	        chatroomRepo.deleteById(chatroomId);
	        return true;
	    }
		// 채팅방이 없으면 삭제 불가
	    return false;
	}
	
}
