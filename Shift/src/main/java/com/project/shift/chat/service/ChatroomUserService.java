package com.project.shift.chat.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.shift.chat.dao.ChatroomUserDAO;
import com.project.shift.chat.dto.ChatroomUserDTO;
import com.project.shift.chat.dto.MessageWithSenderDTO;
import com.project.shift.chat.entity.ChatroomUserEntity;

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
	
	// 특정 채팅방에서 나가기 (상대방 데이터 보존)
	@Transactional
	public boolean deleteChatroomUser(long chatroomUsersId) {
		// 삭제된 행이 있으면 true 반환
		return dao.deleteById(chatroomUsersId);
	}

	////////////임시 API ////////////
	// 특정 채팅방 유저 정보 반환
	@Transactional
	public Optional<ChatroomUserDTO> getChatroomUser(long chatroomId, long userId) {
		return dao.getChatroomUser(chatroomId, userId).map(entity -> ChatroomUserDTO.toDto(entity));
	}
}
