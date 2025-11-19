package com.project.shift.chat.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.project.shift.chat.entity.MessageEntity;
import com.project.shift.chat.repository.MessageRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MessageDAO {

	private final MessageRepository messageRepo;

	public void saveMessage(MessageEntity entity) {
		messageRepo.save(entity);
	}

	public List<MessageEntity> getMessageHistory(long chatroomId, Date createdDateTime) {
		return messageRepo.findByChatroomId(chatroomId, createdDateTime);
	}
		
}
