package com.project.shift.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.shift.chat.entity.ChatroomEntity;

public interface ChatroomRepository extends JpaRepository<ChatroomEntity, Long>{

	List<ChatroomEntity> findByFromId(int userId);
	
	// 두 사용자가 서로 대화하는 두 방의 ID 조회
    @Query("SELECT c.id FROM Chatroom c " +
           "WHERE (c.fromId = :fromId AND c.toId = :toId) " +
           "   OR (c.fromId = :toId AND c.toId = :fromId)")
    List<Integer> findChatroomIdsForUsers(int fromId, int toId);
    
}
