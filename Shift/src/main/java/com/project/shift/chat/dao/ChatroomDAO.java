package com.project.shift.chat.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.project.shift.chat.dto.ChatroomListProjection;
import com.project.shift.chat.entity.ChatroomEntity;
import com.project.shift.chat.repository.ChatroomRepository;
import com.project.shift.chat.repository.MessageRepository;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor  // final 필드에 대한 생성자 자동 생성 (Autowired 대체)
public class ChatroomDAO {

	private final ChatroomRepository chatroomRepo;
	private final MessageRepository messageRepo;

	public Optional<ChatroomEntity> findChatroomById(long chatroomId) {
		return chatroomRepo.findById(chatroomId);
	}
	
	public List<ChatroomListProjection> getUserChatrooms(long userId) {
		return chatroomRepo.findChatroomsByUserId(userId);
	}
	
	public int countUnreadMessages(long chatroomId, long userId) {
		return messageRepo.countUnreadMessages(chatroomId, userId);
	}
	
	public ChatroomEntity saveChatroom(ChatroomEntity entity) {
		// PK null이면 새 엔티티로 판단 → save
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
