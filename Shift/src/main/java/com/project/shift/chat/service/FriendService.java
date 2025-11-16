package com.project.shift.chat.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.shift.chat.dao.FriendDAO;
import com.project.shift.chat.dto.FriendDTO;
import com.project.shift.chat.entity.FriendEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FriendService {

	private final FriendDAO dao;
	
	@Transactional
	public List<FriendDTO> getUserFriends(int userId){
		List<FriendEntity> entityList = dao.getUserFriends(userId);
		List<FriendDTO> dtoList = new ArrayList<FriendDTO>();
		for (FriendEntity e : entityList) {
			dtoList.add(FriendDTO.toDto(e));
		}
		return dtoList;
	}
	
	@Transactional
	public void addFriend(int userId, int friendId) {
		dao.insertFriend(userId, friendId);
	}
	
	@Transactional
	public void deleteFriend(long userId, long friendId) {
		dao.deleteFriend(userId, friendId);
	}

}
