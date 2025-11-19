package com.project.shift.chat.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.shift.chat.dao.ChatroomDAO;
import com.project.shift.chat.dto.ChatroomDTO;
import com.project.shift.chat.dto.ChatroomListDTO;
import com.project.shift.chat.dto.ChatroomListProjection;
import com.project.shift.chat.entity.ChatroomEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatroomService {

	private final ChatroomDAO dao;
	
	// 사용자가 참여한 채팅방 목록 반환
	@Transactional(readOnly = true)
	public List<ChatroomListDTO> getUserChatrooms(long userId){
		List<ChatroomListProjection> chatroomList = dao.getUserChatrooms(userId);
		return chatroomList
			   .stream()
			   .map(p -> {
				   ChatroomListDTO dto = ChatroomListDTO.builder()
                	.chatroomUsersId(p.getChatroomUsersId())
                    .chatroomId(p.getChatroomId())
                    .chatroomName(p.getChatroomName())
                    .lastMsgContent(p.getLastMsgContent())
                    .lastMsgDate(toDate(p.getLastMsgDate()))
                    .lastConnectionTime(toDate(p.getLastConnectionTime()))
                    .createdTime(toDate(p.getCreatedTime()))
                    .connectionStatus(p.getConnectionStatus())
                    .isDarkMode(p.getIsDarkMode())
                    .build();

                // unreadCount 계산
                dto.setUnreadCount(dao.countUnreadMessages(p.getChatroomId(), userId));

                return dto;
            })
            .toList();
	}
	
	// Date 세팅
	private Date toDate(java.sql.Timestamp ts) {
        return ts != null ? new Date(ts.getTime()) : null;
    }
	
	@Transactional
	public ChatroomDTO addChatroom(ChatroomDTO chatroom) {
		// 메시지 전송 시간 세팅
		chatroom.setLastMsgDate(new Date());
		
		// 저장 후 DB에서 생성된 PK 가져오기
		ChatroomEntity entity = ChatroomEntity.toEntity(chatroom);
	    ChatroomEntity savedEntity = dao.saveChatroom(entity);
	    
	    // DTO에 PK 세팅
	    chatroom.setChatroomId(savedEntity.getChatroomId());
	    
	    return chatroom;
	}
	
	// 특정 채팅방에 참여한 모든 사용자, 특정 채팅방 정보 전체 삭제됨
	@Transactional
	public boolean deleteChatroom(long chatroomId) {
		// 삭제된 행이 있으면 true 반환
		return dao.deleteById(chatroomId);
	}
}
