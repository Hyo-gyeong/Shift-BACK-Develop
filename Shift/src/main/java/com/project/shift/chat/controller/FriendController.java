package com.project.shift.chat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.shift.chat.dto.ChatUserDTO;
import com.project.shift.chat.dto.FriendDTO;
import com.project.shift.chat.service.ChatUserService;
import com.project.shift.chat.service.FriendService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class FriendController {
	@Autowired
    FriendService friendService;
	
	@Autowired
	ChatUserService chatUserService;

	// 사용자와 친구인 사용자의 PK 목록 반환
	@GetMapping("/friends/user/{userId}")
    public List<FriendDTO> sendMessage(@PathVariable String userId) {
		log.info("UserId {}", userId);
		int userPK = Integer.parseInt(userId);
		List<FriendDTO> friends = friendService.getUserFriends(userPK);
		if (friends.size() > 0)
			for (FriendDTO f : friends)
				System.out.println(f.toString());
        return friends;
    }
	
	// 친구 pk 리스트를 가지고 친구 정보 반환 - user를 사용하더라도 도메인에 맞게 FriendController에서 구현
	@PostMapping("/friends/info")
	public List<ChatUserDTO> getFriendsInfo(@RequestBody List<Integer> friendPKList){
		log.info(friendPKList.toString());
		List<ChatUserDTO> user = chatUserService.getUserInfoByIds(friendPKList);
		log.info("return size {}", user.size());
		return user;
	}
	
	@GetMapping("/friends/search/{phone}")
	public ChatUserDTO searchUser(@PathVariable String phone) {
		return chatUserService.getUserInfoByPhone(phone);
	}
	
	@PostMapping("/friends/insert")
	public ChatUserDTO addFriend(@RequestBody FriendDTO friendInfo) {
		int userId = Long.valueOf(friendInfo.getUserId()).intValue();
		int friendId = Long.valueOf(friendInfo.getFriendId()).intValue();
		log.info("userid {} friendId {}", userId, friendId);
		friendService.addFriend(userId, friendId);
		ChatUserDTO dto = chatUserService.getUserInfo(userId);
		return dto;
	}
	
}