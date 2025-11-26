package com.project.shift.chat.dao;

import java.util.Date;
import java.util.List;
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
	
	public boolean initChatroomUserExceptKey(long chatroomUserId) {
		// 채팅방이 존재하면 pk제외 데이터 초기화
		if (chatroomUserRepo.existsById(chatroomUserId)) {
			chatroomUserRepo.initChatroomUserExceptKey(chatroomUserId);
	        return true;
	    }
		// 채팅방이 없으면 삭제 불가
	    return false;
	}
	
	public boolean initAllChatroomUsersExceptKey(long chatroomId) {
		// 채팅방이 존재하면 pk제외 데이터 초기화
		if (chatroomUserRepo.existsById(chatroomId)) {
			chatroomUserRepo.initAllChatroomUsersExceptKey(chatroomId);
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

	// 특정 채팅방 유저 정보 반환
	public Optional<ChatroomUserEntity> getChatroomUser(long chatroomId, long userId) {
		return chatroomUserRepo.getChatroomUser(chatroomId, userId);
	}
	
	// 삭제 여부와 관계 없이 두 유저간 생성된 채팅방 반환
	public Optional<Long> getChatroomWithReceiver(List<Long> ids, long countUsers){
		return chatroomUserRepo.findChatroomWithUsers(ids, countUsers);
	}
	
	// 채팅방 생성 시 두 사용자간 삭제된 채팅방 복구
	public void restoreChatroomUser(long chatroomId, long userId, String status, Date now, String chatroomName) {
		chatroomUserRepo.restoreChatroomUser(chatroomId, userId, status, now, chatroomName);
	}
}
