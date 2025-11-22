package com.project.shift.chat.dao;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.project.shift.chat.dto.ChatroomUserDTO;
import com.project.shift.chat.entity.ChatroomUserEntity;
import com.project.shift.chat.repository.ChatroomUserRepository;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class ChatroomUserDAO {

	private final ChatroomUserRepository chatroomUserRepo;
	
	public void addChatroomUser(ChatroomUserEntity entity) {
		chatroomUserRepo.save(entity);
	}
	
	public boolean deleteById(long chatroomId) {
		// 채팅방이 존재하면 삭제
		if (chatroomUserRepo.existsById(chatroomId)) {
			chatroomUserRepo.deleteById(chatroomId);
	        return true;
	    }
		// 채팅방이 없으면 삭제 불가
	    return false;
	}
	
	public void updateChatUserInfo(ChatroomUserDTO userDTO) {
		chatroomUserRepo.updateChatUserInfo(userDTO.getConnectionStatus(),
											userDTO.getLastConnectionTime(),
											userDTO.getChatroomUserId());
	}

	////////////임시 API ////////////
	// 특정 채팅방 유저 정보 반환
	public Optional<ChatroomUserEntity> getChatroomUser(long chatroomId, long userId) {
		return chatroomUserRepo.getChatroomUser(chatroomId, userId);
	}
}
