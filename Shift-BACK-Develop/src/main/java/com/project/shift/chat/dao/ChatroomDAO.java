package com.project.shift.chat.dao;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.project.shift.chat.dto.ChatroomListProjection;
import com.project.shift.chat.dto.MessageSearchResultProjection;
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
	
	public boolean initChatroomExceptKey(long chatroomId) {
		// 채팅방이 존재하면 삭제
		if (chatroomRepo.existsById(chatroomId)) {
	        // 값 변경
			chatroomRepo.initChatroomExceptKey(chatroomId);
			return true;
	    }
		// 채팅방이 없으면 삭제 불가
	    return false;
	}
	
	// 채팅 검색 - 채팅에 참여한 사용자 이름
	public List<ChatroomListProjection> searchChatroomUsersName(String input, long userId){
		return chatroomRepo.findChatroomUsersBySearchInput(input, userId);
	}
	
	// 채팅 검색 - 참여한 모든 채팅방의 메시지 내용
	public List<MessageSearchResultProjection> searchChatroomMessages(String input, long userId){
		return chatroomRepo.findChatroomMessagesBySearchInput(input, userId);
	}
	
	// 채팅방 마지막 메시지와 보낸 시간 업데이트
	public void updateLastMsgAndDate(long chatroomId, String content, Date date) {
		chatroomRepo.updateLastMsgAndDate(chatroomId, content, date);
	}
	
}
