package com.project.shift.chat.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.shift.chat.dao.ChatroomDAO;
import com.project.shift.chat.dto.ChatroomDTO;
import com.project.shift.chat.entity.ChatroomEntity;

@Service
public class ChatroomService {

	@Autowired
	ChatroomDAO dao;
	
	@Transactional
	public List<ChatroomDTO> getUserChatrooms(int userId){
		List<ChatroomEntity> entityList = dao.getUserChatrooms(userId);
		List<ChatroomDTO> dtoList = new ArrayList<ChatroomDTO>();
		for (ChatroomEntity e : entityList) {
			dtoList.add(ChatroomDTO.toDto(e));
		}
		return dtoList;
	}
	
	@Transactional
	public List<Integer> findChatroomIdsForUsers(int fromId, int toId) {
		return dao.findChatroomIdsForUsers(fromId, toId);
	}
	
	@Transactional
	public ChatroomDTO addChatroom(ChatroomDTO dto) {
		ChatroomEntity entity = dao.insertChatroom(ChatroomEntity.toEntity(dto));
		return ChatroomDTO.toDto(entity);
	}
	
	@Transactional
	public boolean deleteChatroom(long chatroomId) {
		return dao.deleteById(chatroomId); // 삭제된 행이 있으면 true
	}
}
