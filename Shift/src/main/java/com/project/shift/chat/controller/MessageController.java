package com.project.shift.chat.controller;

import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.shift.chat.dto.ChatroomListDTO;
import com.project.shift.chat.dto.MessageDTO;
import com.project.shift.chat.dto.MessageUserDTO;
import com.project.shift.chat.service.MessageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/messages")
public class MessageController {
	
	private final MessageService messageService;
    
	@MessageMapping("/send")
    public void sendMessage(@Payload MessageUserDTO dto) {
		messageService.sendAndSaveMessage(dto.getMessageDTO(), dto.getChatroomUserDTO());
    }
	
	// 채팅방 최초 생성 시간 이후의 채팅 메시지 기록 반환
	@GetMapping("/history")
	public List<MessageDTO> getMessageHistory(@RequestBody ChatroomListDTO chatroomListDto) {
	    return messageService.getMessageHistory(chatroomListDto);
	}
	
}