package com.project.shift.chat.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.project.shift.chat.entity.FriendEntity;
import com.project.shift.chat.repository.FriendRepository;

@Component
public class FriendDAO {

	@Autowired
	FriendRepository friendRepo;

	public List<FriendEntity> getUserFriends(int userId) {
		return friendRepo.findByUserId(userId);
	}

	public void insertFriend(int userId, int friendId) {
		friendRepo.insertFriend(userId, friendId);
	}
	
	
}
