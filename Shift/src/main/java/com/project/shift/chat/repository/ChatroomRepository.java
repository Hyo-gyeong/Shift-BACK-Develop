package com.project.shift.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.shift.chat.entity.ChatroomEntity;

public interface ChatroomRepository extends JpaRepository<ChatroomEntity, Long>{

	List<ChatroomEntity> findByFromUserId(int userId);
	
	// 두 사용자가 서로 대화하는 방의 ID 조회(사용자별로 chatroomId가 다름)
    @Query("SELECT c.id FROM ChatroomEntity c " +
           "WHERE (c.fromUserId = :fromId AND c.toUserId = :toId) " +
           "		OR (c.fromUserId = :toId AND c.toUserId = :fromId)")
    List<Integer> findChatroomIdsForUsers(int fromId, int toId);
    
    // SHOP-016 : senderId 기준으로 receiverId 조회
    @Query("""
            SELECT 
                CASE 
                    WHEN c.fromUserId = :senderId THEN c.toUserId
                    ELSE c.fromUserId 
                END
            FROM ChatroomEntity c
            WHERE c.id = :chatroomId
        """)
        Long findReceiverIdByChatroom(
                @Param("chatroomId") Long chatroomId,
                @Param("senderId") Long senderId
        );
}
