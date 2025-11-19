package com.project.shift.chat.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.shift.chat.dto.ChatUserSearchResultDTO;
import com.project.shift.chat.service.ChatUserService;
import com.project.shift.global.jwt.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chat/users")
public class ChatUserController {

	private final ChatUserService chatUserService;
	private final JwtService jwtService;

	// 전화번호로 사용자 검색 및 친구여부 반환
	@GetMapping("/search/{phone}")
	public ChatUserSearchResultDTO searchUser(HttpServletRequest request, @PathVariable String phone) {
		// jwt에서 현재 사용자의 토큰 추출
		String token = jwtService.extractTokenFromRequest(request);
        // 토큰에서 현재 사용자의 PK 추출
		long userId = jwtService.extractUserIdFromValidToken(token);
				
		return chatUserService.searchUserByPhone(userId, phone);		
	}
	
}