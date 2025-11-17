package com.project.shift.chat.controller;

import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.shift.chat.dto.ChatroomDTO;
import com.project.shift.chat.service.ChatroomService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chatrooms")
public class ChatroomController {
	
//	private final ChatroomService chatroomService;
//	
//	@GetMapping("/users/{userId}")
//	public List<ChatroomDTO> getUserChatroomList(@PathVariable int userId){
//		List<ChatroomDTO> chatrooms = chatroomService.getUserChatrooms(userId);
//		return chatrooms;
//	}
//	
//	@PostMapping("/create")
//	public ChatroomDTO createChatroom(@RequestBody ChatroomDTO payload) {
//		long fromUserId = payload.getFromUserId();
//		long toUserId = payload.getToUserId();
//		String chatroomName = payload.getChatroomName();
//		
//		ChatroomDTO newChatRoom = new ChatroomDTO();
//		newChatRoom.setFromUserId(fromUserId);
//		newChatRoom.setToUserId(toUserId);
//		newChatRoom.setChatroomName(chatroomName);
//		newChatRoom.setConnectionTime(new Date());
//		
//		ChatroomDTO createdChatroom = chatroomService.addChatroom(newChatRoom);
//		
//		return createdChatroom;
//	}
//	
//	@DeleteMapping("/delete/{roomId}")
//	public ResponseEntity<?> deleteChatroom(@PathVariable int roomId) {
//	    try {
//	        boolean deleted = chatroomService.deleteChatroom(roomId);
//	        if (deleted) {
//	            return ResponseEntity.ok("Chatroom deleted successfully");
//	        } else {
//	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//	                                 .body("Chatroom not found");
//	        }
//	    } catch (Exception e) {
//	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//	                             .body("Error deleting chatroom: " + e.getMessage());
//	    }
//	}

}
