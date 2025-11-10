package com.project.shift.chat.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.project.shift.chat.entity.MessageEntity;
import com.project.shift.chat.repository.MessageRepository;

@Component
public class MessageDAO {

	@Autowired
	MessageRepository messageRepo;

	public void insertMessage(MessageEntity entity) {
		messageRepo.save(entity);
	}

	public List<MessageEntity> getMessagesBetweenUsers(List<Integer> chatroomIds) {
		return messageRepo.findMessagesByChatroomIds(chatroomIds);
	}
	
	
}
