package com.project.shift.chat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.shift.chat.dao.FriendDAO;
import com.project.shift.chat.dto.FriendDTO;
import com.project.shift.chat.dto.FriendInfoDTO;
import com.project.shift.chat.entity.FriendEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FriendService {

//	private final FriendDAO dao;
//	
//	@Transactional(readOnly = true)
//	public List<FriendInfoDTO> getUserFriends(long userId){
//		return dao.getUserFriends(userId);
//	}
//	
//	@Transactional
//	public void addFriendship(FriendDTO dto) {
//		FriendEntity entity = FriendEntity.toEntity(dto);
//		dao.saveFriendship(entity);
//		return;
//	}
//	
//	@Transactional
//	public boolean deleteFriend(long friendshipId) {
//		// 삭제된 행이 있으면 true 반환
//		return dao.deleteFriend(friendshipId);
//	}

}
