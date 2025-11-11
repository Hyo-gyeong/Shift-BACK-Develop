package com.project.shift.chat.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.shift.chat.dao.ChatUserDAO;
import com.project.shift.chat.dto.ChatUserDTO;
import com.project.shift.chat.entity.ChatUserEntity;

@Service
public class ChatUserService {

	@Autowired
	ChatUserDAO dao;
	
	public ChatUserDTO getUserInfo(int id){
		ChatUserEntity entity = dao.getUserInfo(id);
		if (entity != null) {
			ChatUserDTO dto = ChatUserDTO.toDto(entity);
			return dto;
		}
		return null;
	}
	
	public List<ChatUserDTO> getUserInfoByIds(List<Integer> userIds){
		List<ChatUserEntity> entityList = dao.getUserInfoByIds(userIds);
		List<ChatUserDTO> dtoList = new ArrayList<ChatUserDTO>();
		for (ChatUserEntity e : entityList) {
			dtoList.add(ChatUserDTO.toFriendUserDTO(e));
		}
		return dtoList;
	}

	public ChatUserDTO getUserInfoByPhone(String phone) {
		ChatUserEntity entity = dao.getUserInfoByPhone(phone);
		ChatUserDTO dto = ChatUserDTO.toDto(entity);
		return dto;
	}
}
