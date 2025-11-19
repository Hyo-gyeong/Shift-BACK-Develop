package com.project.shift.chat.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.shift.chat.dao.ChatUserDAO;
import com.project.shift.chat.dao.FriendDAO;
import com.project.shift.chat.dto.ChatUserSearchResultDTO;
import com.project.shift.chat.entity.ChatUserEntity;
import com.project.shift.chat.exception.UserNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatUserService {

	private final ChatUserDAO chatUserDAO;
	private final FriendDAO friendDAO;
	
	@Transactional(readOnly = true)
	public ChatUserSearchResultDTO searchUserByPhone(long userId, String phone) {
		ChatUserEntity entity = chatUserDAO.getUserInfoByPhone(phone);
		if (entity == null) {
	        throw new UserNotFoundException("해당 전화번호의 사용자를 찾을 수 없습니다.");
		}
		// 검색된 사용자와의 친구여부 포함하여 반환
		long friendId = entity.getUserId();
		boolean ifFriend = friendDAO.checkIfFriend(userId, friendId);
		
		return ChatUserSearchResultDTO.builder()
				.ifFriend(ifFriend)
				.userId(entity.getUserId())
				.loginId(entity.getLoginId())
				.name(entity.getName())
				.phone(entity.getPhone())
				.build();
	}
	
}
