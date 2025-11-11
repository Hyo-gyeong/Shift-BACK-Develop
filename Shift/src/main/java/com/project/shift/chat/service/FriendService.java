package com.project.shift.chat.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.shift.chat.dao.FriendDAO;
import com.project.shift.chat.dto.FriendDTO;
import com.project.shift.chat.entity.FriendEntity;

@Service
public class FriendService {

	@Autowired
	FriendDAO dao;
	
	public List<FriendDTO> getUserFriends(int userId){
		List<FriendEntity> entityList = dao.getUserFriends(userId);
		List<FriendDTO> dtoList = new ArrayList<FriendDTO>();
		for (FriendEntity e : entityList) {
			dtoList.add(FriendDTO.toDto(e));
		}
		return dtoList;
	}
	
	public void addFriend(int userId, int friendId) {
		dao.insertFriend(userId, friendId);
	}
	
	public void deleteFriend(long userId, long friendId) {
		dao.deleteFriend(userId, friendId);
	}

}
