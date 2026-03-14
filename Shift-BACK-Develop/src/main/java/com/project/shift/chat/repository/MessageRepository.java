package com.project.shift.chat.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.shift.chat.dto.UnreadCountProjection;
import com.project.shift.chat.entity.MessageEntity;

import jakarta.transaction.Transactional;

public interface MessageRepository extends JpaRepository<MessageEntity, Long>{

	// 특정 사용자가 읽지 않은 특정 채팅방의 메시지 개수 반환 -> 단건 조회로 사용자가 접속한 채팅방마다 호출해야하는 N+1문제 발생 - 삭제 예정
	@Query(value = """
			select count(*)
			from messages m, chatroom_users cu
			where cu.user_id = :userId and
				  cu.chatroom_id = :chatroomId and
				  m.chatroom_id = cu.chatroom_id and
				  m.send_date >= GREATEST(cu.last_connection_time, cu.created_time) and
				  m.user_id <> :userId
			""", nativeQuery = true)
	int countUnreadMessages(@Param("chatroomId") long chatroomId, @Param("userId") long userId);
	
	@Query("""
		    SELECT cu.chatroom.chatroomId as chatroomId, COUNT(m) as unreadCount
		    FROM MessageEntity m
		    JOIN ChatroomUserEntity cu
		        ON cu.chatroom.chatroomId = m.chatroom.chatroomId
		        AND cu.user.userId = :userId
		    WHERE m.chatroom.chatroomId IN :chatroomIds
		      AND m.sendDate > cu.lastConnectionTime   -- 내가 마지막으로 접속한 이후 메시지만
		      AND m.user.userId != :userId             -- 내가 보낸 메시지 제외
		    GROUP BY cu.chatroom.chatroomId           -- 채팅방별로 카운트
			""")
	List<UnreadCountProjection> countUnreadMessagesBatch(@Param("chatroomIds") List<Long> chatroomIds,
		                                                     @Param("userId") long userId);

	/////////////////////////////////////////////////////////////////////////////////////////
	
	// 채팅방 최초 생성 시간 이후의 메시지 반환
	@Query("""
			SELECT m FROM MessageEntity m
			WHERE m.chatroom.chatroomId = :chatroomId AND
				  m.sendDate >= :createdDateTime
			""")
	List<MessageEntity> findByChatroomId(@Param("chatroomId") long chatroomId,
										 @Param("createdDateTime") Date createdDateTime);

	// 마지막 접속 시간 이후의 메시지 읽음 처리
	@Modifying
	@Transactional
	@Query("""
			UPDATE MessageEntity m
			SET m.unreadCount = m.unreadCount-1
			WHERE m.chatroom.chatroomId = :chatroomId AND
				  m.unreadCount > 0 AND
				  m.sendDate >= :lastConnectionTime AND
				  m.user.userId <> :userId
			""")
	void markMessagesAsRead(@Param("chatroomId") long chatroomId, @Param("lastConnectionTime") Date lastConnectionTime, @Param("userId") long userId);
	
}
