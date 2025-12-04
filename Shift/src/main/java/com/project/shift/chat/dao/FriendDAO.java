package com.project.shift.chat.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.project.shift.chat.dto.FriendInfoDTO;
import com.project.shift.chat.entity.FriendEntity;
import com.project.shift.chat.repository.FriendRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FriendDAO {

	private final FriendRepository friendRepo;

	public boolean checkIfFriend(long userId, long friendId) {
		return friendRepo.existsByUserIdAndFriendId(userId, friendId);
	}
	
	public List<FriendInfoDTO> getUserFriends(long userId) {
		return friendRepo.getFriendsList(userId);
	}

	public void saveFriendship(FriendEntity entity) {
		friendRepo.save(entity);
		return;
	}
	
	public boolean deleteFriend(long friendshipId) {
		// 친구 관계가 존재하면 삭제
		if (friendRepo.existsById(friendshipId)) {
			friendRepo.deleteById(friendshipId);
			return true;
		}
		// 친구 관계가 없으면 삭제 불가
		return false;
	}

    // 탈퇴하는 사용자의 모든 친구 관계 삭제
    public void deleteAllFriends(long userId) {
        friendRepo.deleteFriendship(userId);
    }
}
