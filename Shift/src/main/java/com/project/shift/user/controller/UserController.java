package com.project.shift.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.project.shift.chat.dto.ChatUserDTO;
import com.project.shift.chat.service.ChatUserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class UserController {
	
	// 추후 UserService로 변경 예정
	@Autowired
	ChatUserService chatUserService;
	
	// 전화번호로 사용자 검색 (친구 추가 용도). 추후 ChatUserDTO를 UserDTO로 변경 예정
	@GetMapping("/users/search/{phone}")
	public ChatUserDTO searchUser(@PathVariable String phone) {
		return chatUserService.getUserInfoByPhone(phone);
	}
	
}
