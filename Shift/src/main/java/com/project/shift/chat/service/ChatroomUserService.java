package com.project.shift.chat.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.shift.chat.dao.ChatroomUserDAO;
import com.project.shift.chat.dto.ChatroomUserDTO;
import com.project.shift.chat.dto.MessageWithSenderDTO;
import com.project.shift.chat.entity.ChatroomUserEntity;
import com.project.shift.chat.exception.UserNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatroomUserService {

	private final ChatroomUserDAO dao;
	
	// 특정 채팅방에 참여
	@Transactional
	public void addChatroomUsers(MessageWithSenderDTO dto, long chatroomId) {
		// 채팅 생성자 생성 후 저장
		ChatroomUserDTO sender = dto.getSender();		
		sender.setChatroomId(chatroomId);
		sender.setConnectionStatus("ON");
		dao.addChatroomUser(ChatroomUserEntity.toEntity(sender));
		
		// 채팅 수신자 생성 후 저장
		ChatroomUserDTO receiver = ChatroomUserDTO.builder()
									.chatroomId(chatroomId)
									.connectionStatus("OF")
									.userId(dto.getReceiverId())
									.isDarkMode("N")
									.chatroomName(dto.getSenderName()+"님과의 채팅")
									.build();
		dao.addChatroomUser(ChatroomUserEntity.toEntity(receiver));
		return;
	}
	
	// 특정 채팅방에서 특정 사용자만 나가기 (사용자 key 보존, 상대방 데이터 보존)
	@Transactional
	public boolean deleteChatroomUser(long chatroomUsersId) {
		// 삭제된(초기화된) 행이 있으면 true 반환
		return dao.initChatroomUserExceptKey(chatroomUsersId);
	}
	
	// 특정 채팅방에 참여한 모든 사용자 나가기 (생성된 key만 보존)
	@Transactional
	public boolean deleteAllChatroomUsers(long chatroomId) {
		// 삭제된(초기화된) 행이 있으면 true 반환
		return dao.initAllChatroomUsersExceptKey(chatroomId);
	}

	// 특정 채팅방 유저 정보 반환
	@Transactional(readOnly = true)
	public Optional<ChatroomUserDTO> getChatroomUser(long chatroomId, long userId) {
		Optional<ChatroomUserEntity> entityOpt = dao.getChatroomUser(chatroomId, userId);
	    if (entityOpt.isEmpty()) {
	        throw new UserNotFoundException("특정 채팅방의 유저 정보가 없습니다.");
	    }

	    return entityOpt.map(ChatroomUserDTO::toDto);
	}
	
	@Transactional(readOnly = true)
	public Optional<ChatroomUserDTO> getChatroomWithReceiver(long userId, long receiverId){
		List<Long> ids = new ArrayList<>();
		ids.add(userId);
		ids.add(receiverId);
		Optional<Long> chatroomIdOpt = dao.getChatroomWithUsers(ids, ids.size());
		if (chatroomIdOpt.isEmpty()) { // 한 번도 채팅을 한 적이 없는 사용자들
			return Optional.empty();
		} else { // 채팅방 삭제 여부와 관계 없이 한 번이라도 채팅을 한 사용자들
			return getChatroomUser(chatroomIdOpt.get(), userId);
		}
	}
}
