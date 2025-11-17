package com.project.shift.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.shift.chat.dto.ChatUserDTO;
import com.project.shift.chat.service.ChatUserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chat/users")
public class ChatUserController {

	// 추후 UserService로 변경 예정
	private final ChatUserService chatUserService;

	// 전화번호로 사용자 검색 (친구 추가 용도). 추후 ChatUserDTO를 UserDTO로 변경 예정
	@GetMapping("/search/{phone}")
	public ChatUserDTO searchUser(@PathVariable String phone) {
		return chatUserService.getUserInfoByPhone(phone);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getUserInfo (@PathVariable int id){
		ChatUserDTO user = chatUserService.getUserInfo(id);
		if (user != null) {
			log.info("user pk {}", user.getUserId());
            // 유저가 존재하면 200 OK + JSON 반환
            return ResponseEntity.ok(user);
        } else {
            // 유저가 없으면 404 Not Found + 메시지 반환
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("User not found");
        }
	}
}