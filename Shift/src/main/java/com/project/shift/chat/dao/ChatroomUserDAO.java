package com.project.shift.chat.dao;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.project.shift.chat.dto.ChatroomListProjection;
import com.project.shift.chat.dto.ChatroomUserDTO;
import com.project.shift.chat.entity.ChatroomUserEntity;
import com.project.shift.chat.repository.ChatroomUserRepository;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class ChatroomUserDAO {

	private final ChatroomUserRepository chatroomUserRepo;
	
	public void addChatroomUser(ChatroomUserEntity entity) {
		chatroomUserRepo.save(entity);
	}
	
	public boolean initChatroomUserExceptKey(long chatroomUserId) {
		// 채팅방이 존재하면 pk제외 데이터 초기화
		if (chatroomUserRepo.existsById(chatroomUserId)) {
			chatroomUserRepo.initChatroomUserExceptKey(chatroomUserId);
	        return true;
	    }
		// 채팅방이 없으면 삭제 불가
	    return false;
	}
	
	public boolean initAllChatroomUsersExceptKey(long chatroomId) {
		// 채팅방이 존재하면 pk제외 데이터 초기화
		if (chatroomUserRepo.existsById(chatroomId)) {
			chatroomUserRepo.initAllChatroomUsersExceptKey(chatroomId);
			return true;
	    }
		// 채팅방이 없으면 삭제 불가
	    return false;
	}
	
	public void updateChatUserInfo(ChatroomUserDTO userDTO) {
		chatroomUserRepo.updateChatUserInfo(userDTO.getConnectionStatus(),
											userDTO.getLastConnectionTime(),
											userDTO.getChatroomUserId());
	}

	// 특정 채팅방 유저 정보 반환
	public Optional<ChatroomUserEntity> getChatroomUser(long chatroomId, long userId) {
		return chatroomUserRepo.getChatroomUser(chatroomId, userId);
	}
	
	// 삭제 여부와 관계 없이 두 유저간 생성된 채팅방 반환
	public Optional<Long> getChatroomWithReceiver(List<Long> ids, long countUsers){
		return chatroomUserRepo.findChatroomWithUsers(ids, countUsers);
	}
	
	// 채팅방 생성 시 두 사용자간 삭제된 채팅방 복구
	public void restoreChatroomUser(long chatroomId, long userId, String status, Date now, String chatroomName) {
		chatroomUserRepo.restoreChatroomUser(chatroomId, userId, status, now, chatroomName);
	}
	
	// 채팅방 connectionStatus가 DL인지 확인
	public boolean checkIfChatroomDeleted (long chatroomUserId, long userId) {
		int rslt = chatroomUserRepo.checkIfChatroomDeleted(chatroomUserId, userId);
		return rslt > 0;
	}
	
	// 채팅방 접속 상태를 DL에서 OF로 변경
	public void updateReceiverConnectionStatus(long chatroomId, long userId, Date now) {
		chatroomUserRepo.updateReceiverConnectionStatus(chatroomId, userId, now);
	}
	
	// 채팅방 이름 변경
	public int updateChatroomName(long chatroomUserId, String newChatroomName) {
		return chatroomUserRepo.updateChatroomName(chatroomUserId, newChatroomName);
	}
	
	// CHATROOM-08 : 특정 채팅방 정보 반환
	public Optional<ChatroomListProjection> getChatroomListView(long chatroomUserId){
		return chatroomUserRepo.findChatroomByChatroomUserId(chatroomUserId);
	}
	
	// chatroomUserId와 userId로 해당 채팅방의 모든 ReceiverId를 반환하는 함수
	// 메시지가 전송됐다는 알림을 모든 수신자들에게 보내기 위한 함수 (실시간 채팅방 목록 관련)
	public List<Long> getReceiverId(long chatroomUserId, long userId) {
		return chatroomUserRepo.getReceiverId(chatroomUserId, userId);
	}
	
	// 현재 채팅방에서 나를 제외하고 접속 중인 사용자의 수를 반환
	public int countOtherUsersOnline(long chatroomId, long userId) {
		return chatroomUserRepo.countOtherUsersOnline(chatroomId, userId);
	}

    // 회원 탈퇴 시 처리
    // 탈퇴하는 사용자의 모든 채팅방 관계 삭제
    public void deleteChatroomUsersByUserId(long userId) {
        chatroomUserRepo.updateStatusToDeletedByUserId(userId);
    }
}
