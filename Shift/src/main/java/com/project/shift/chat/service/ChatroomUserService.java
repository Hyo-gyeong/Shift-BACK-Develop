package com.project.shift.chat.service;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.shift.chat.dao.ChatroomUserDAO;
import com.project.shift.chat.dto.ChatroomUserDTO;
import com.project.shift.chat.entity.ChatroomUserEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatroomUserService {

	private final ChatroomUserDAO dao;
	
	// 특정 채팅방에 참여
	@Transactional
	public void addChatroomUser(ChatroomUserDTO dto, long chatroomId) {
		dto.setCreatedTime(new Date());
		// 마지막 접속시간은 기본값으로 처음 생성 시간과 동일하게 세팅
		dto.setLastConnectionTime(new Date());
		dto.setChatroomId(chatroomId);
		ChatroomUserEntity entity = ChatroomUserEntity.toEntity(dto);
		dao.addChatroomUser(entity);
	}
	
	// 특정 채팅방에서 나가기 (상대방 데이터 보존)
	@Transactional
	public boolean deleteChatroomUser(long chatroomUsersId) {
		// 삭제된 행이 있으면 true 반환
		return dao.deleteById(chatroomUsersId);
	}
}
