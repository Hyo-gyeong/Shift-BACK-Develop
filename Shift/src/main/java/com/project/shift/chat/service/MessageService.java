package com.project.shift.chat.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.shift.chat.dao.MessageDAO;
import com.project.shift.chat.dto.ChatroomListDTO;
import com.project.shift.chat.dto.ChatroomUserDTO;
import com.project.shift.chat.dto.MessageDTO;
import com.project.shift.chat.entity.MessageEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageService {
	
	private final MessageDAO messageDAO;
	private final SimpMessagingTemplate messagingTemplate;

	// 메시지 DB 저장
	@Transactional
	public void addMessage(MessageDTO message) {
		messageDAO.saveMessage(MessageEntity.toEntity(message));
	}
	
	// 채팅방 최초 접속 시간 이후 모든 채팅방 메시지 반환
	@Transactional(readOnly = true)
	public List<MessageDTO> getMessageHistory(ChatroomListDTO dto){
		long chatroomId = dto.getChatroomId();
		Date createdDateTime = dto.getCreatedTime();
		List<MessageEntity> entityList = messageDAO.getMessageHistory(chatroomId, createdDateTime);
		List<MessageDTO> dtoList = new ArrayList<MessageDTO>();
		for (MessageEntity e : entityList) {
			dtoList.add(MessageDTO.toDto(e));
		}
		return dtoList;
	}
	
	// 채팅 메시지 전송시 메시지 DB에 저장 및 브로드캐스팅
	@Transactional
	public void sendAndSaveMessage(MessageDTO messageDTO, ChatroomUserDTO userDTO) {		
		switch (messageDTO.getType()) {
	        case JOIN :
	        	// 접속 상태 ON으로 세팅
	        	userDTO.setConnectionStatus("ON");
	        	// 채팅방 최초 생성 시간 이후 모든 메시지 읽음 처리
	        	messageDAO.markMessagesAsRead(messageDTO.getChatroomId(), userDTO.getLastConnectionTime());
	            break;	
	        case LEAVE :
	            // 접속 상태 OF로 세팅
	        	userDTO.setConnectionStatus("OF");
	        	// 채팅방 마지막 접속 시간을 현재 시간으로 변경
	        	userDTO.setLastConnectionTime(new Date());
	            break;
			case CHAT :
	        	// 메시지를 DB에 저장하는 로직 호출
	        	messageDAO.saveMessage(MessageEntity.toEntity(messageDTO));
	        	break;
	        default :
	            break;
		}
		
		// 메시지 브로드캐스팅 로직 호출
		broadcastToChatroom(messageDTO);
	}
	
	private void broadcastToChatroom(MessageDTO messageDTO) {
		try {
			messagingTemplate.convertAndSend("/sub/messages/" + messageDTO.getChatroomId(), messageDTO);
		} catch(Exception e) {
			// 예외 던지기
		}
		return;
	}

}
