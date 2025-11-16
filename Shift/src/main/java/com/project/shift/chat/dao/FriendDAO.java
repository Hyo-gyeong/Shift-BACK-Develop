package com.project.shift.chat.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.project.shift.chat.entity.FriendEntity;
import com.project.shift.chat.repository.ChatroomRepository;
import com.project.shift.chat.repository.FriendRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FriendDAO {

	private final FriendRepository friendRepo;

	public List<FriendEntity> getUserFriends(int userId) {
		return friendRepo.findByUserId(userId);
	}

	public void insertFriend(int userId, int friendId) {
		friendRepo.insertFriend(userId, friendId);
	}
	
	public void deleteFriend(long userId, long friendId) {
		friendRepo.deleteFriend(userId, friendId);
	}
	
}
