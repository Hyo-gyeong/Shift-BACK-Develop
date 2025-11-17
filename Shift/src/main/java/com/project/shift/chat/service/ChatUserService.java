package com.project.shift.chat.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.shift.chat.dao.ChatUserDAO;
import com.project.shift.chat.dto.ChatUserDTO;
import com.project.shift.chat.entity.ChatUserEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatUserService {

	private final ChatUserDAO dao;
	
	@Transactional(readOnly = true)
	public ChatUserDTO getUserInfo(int id){
		ChatUserEntity entity = dao.getUserInfo(id);
		if (entity != null) {
			ChatUserDTO dto = ChatUserDTO.toDto(entity);
			return dto;
		}
		return null;
	}
	
	@Transactional(readOnly = true)
	public List<ChatUserDTO> getUserInfoByIds(List<Integer> userIds){
		List<ChatUserEntity> entityList = dao.getUserInfoByIds(userIds);
		List<ChatUserDTO> dtoList = new ArrayList<ChatUserDTO>();
		for (ChatUserEntity e : entityList) {
			dtoList.add(ChatUserDTO.toFriendUserDTO(e));
		}
		return dtoList;
	}
	
	@Transactional(readOnly = true)
	public ChatUserDTO getUserInfoByPhone(String phone) {
		ChatUserEntity entity = dao.getUserInfoByPhone(phone);
		ChatUserDTO dto = ChatUserDTO.toDto(entity);
		return dto;
	}
}
