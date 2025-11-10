package com.project.shift.chat.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.shift.chat.dao.ChatroomDAO;
import com.project.shift.chat.dao.MessageDAO;
import com.project.shift.chat.dto.MessageDTO;
import com.project.shift.chat.entity.MessageEntity;

@Service
public class MessageService {
	
	@Autowired
	MessageDAO messageDAO;
	
	@Autowired
	ChatroomDAO chatroomDAO;
	
	public void addMessage(MessageDTO message) {
		messageDAO.insertMessage(MessageEntity.toEntity(message));
	}
	
	public List<MessageDTO> getMessagesBetweenUsers(int fromId, int toId){
		List<Integer> chatroomIds = chatroomDAO.findChatroomIdsForUsers(fromId, toId);
		
		if (chatroomIds.isEmpty()) {
            return Collections.emptyList();
        }
		
		List<MessageEntity> entityList = messageDAO.getMessagesBetweenUsers(chatroomIds);
		List<MessageDTO> dtoList = new ArrayList<MessageDTO>();
		for (MessageEntity e : entityList) {
			dtoList.add(MessageDTO.toDto(e));
		}
		return dtoList;
	}

}
