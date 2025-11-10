package com.project.shift.chat.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.shift.chat.entity.FriendEntity;
import com.project.shift.chat.repository.FriendRepository;

@Service
public class FriendDAO {

	@Autowired
	FriendRepository friendRepo;
	
	public List<FriendEntity> getUserFriends (int userId){
		return friendRepo.findByUserId(userId);
	}
	
	public void insertFriend(int userId, int friendId) {
		FriendEntity friend = new FriendEntity();
        friend.setUserId(userId);
        friend.setFriendId(friendId);
        friendRepo.save(friend);
	}
	
}
