package com.project.shift.chat.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.project.shift.chat.entity.ChatroomUserEntity;

public interface ChatroomUserRepository extends JpaRepository<ChatroomUserEntity, Long>{

	// 사용자 채팅방 접속 정보 수정
	@Modifying
	@Transactional
	@Query("""
			UPDATE ChatroomUserEntity c 
			SET c.connectionStatus = :status, c.lastConnectionTime = :time 
			WHERE c.chatroomUserId = :id
			""")
	void updateChatUserInfo(@Param("status") String status,
							@Param("time") Date time,
							@Param("id") long id);
	
	// 특정 채팅방 유저 정보 반환
	@Query("""
			SELECT c FROM ChatroomUserEntity c
			WHERE c.chatroomId = :id AND c.userId = :userId
			""")
	Optional<ChatroomUserEntity> getChatroomUser(@Param("id") long id,
											  	 @Param("userId") long userId);
	
	// 특정 채팅방의 특정 유저의 채팅방 삭제시 pk, fk 빼고 전부 초기화
	@Modifying
	@Transactional
	@Query("""
			UPDATE ChatroomUserEntity c 
			SET c.chatroomName = null, 
			c.lastConnectionTime = null, 
			c.createdTime = null, 
			c.connectionStatus = 'DL', 
			c.isDarkMode = 'N' 
			WHERE c.chatroomUserId = :id
			""")
	void initChatroomUserExceptKey(@Param("id") long id);
	
	// 특정 채팅방의 모든 유저의 채팅방 삭제시 pk, fk 빼고 전부 초기화
	@Modifying
	@Transactional
	@Query("""
			UPDATE ChatroomUserEntity c 
			SET c.chatroomName = null, 
			c.lastConnectionTime = null, 
			c.createdTime = null, 
			c.connectionStatus = 'DL', 
			c.isDarkMode = 'N' 
			WHERE c.chatroomId = :id
			""")
	void initAllChatroomUsersExceptKey(@Param("id") long id);

	// 두 사용자가 속한 채팅방 ID 반환
	@Query("""
			SELECT c.chatroomId
		    FROM ChatroomUserEntity c
		    WHERE c.userId IN :ids
		    GROUP BY c.chatroomId
		    HAVING COUNT(DISTINCT c.userId) = :countUsers 
			""")
	Optional<Long> findChatroomWithUsers(@Param("ids") List<Long> ids, @Param("countUsers") long countUsers);
	
	// 채팅방 생성 시 두 사용자간 삭제된 채팅방 복구
	@Modifying
	@Transactional
	@Query("""
		    UPDATE ChatroomUserEntity c 
		    SET c.createdTime = :now,
			    c.lastConnectionTime = :now,
			    c.connectionStatus = :connectionStatus,
			    c.chatroomName = :chatroomName
			WHERE c.chatroomId = :chatroomId
				AND c.userId = :userId
				AND c.connectionStatus = 'DL'
			""")
	void restoreChatroomUser(@Param("chatroomId") long chatroomId,
							 @Param("userId") long userId,
							 @Param("connectionStatus") String connectionStatus,
							 @Param("now") Date now,
							 @Param("chatroomName") String chatroomName);
	
	// 상대방의 채팅방 삭제여부 반환
	@Query("""
		    SELECT COUNT(c)
		    FROM ChatroomUserEntity c
		    WHERE c.connectionStatus = 'DL'
		      AND c.chatroomId = :id
		      AND c.userId != :userId
		""")
	int checkIfChatroomDeleted(@Param("id") long id, @Param("userId") long userId);

	// 상대방의 채팅방 접속 상태를 'DL'에서 'OF'로 변경 및 채팅방 생성 시간과 마지막 접속시간 변경
	@Modifying
	@Transactional
	@Query("""
			UPDATE ChatroomUserEntity c 
			SET c.connectionStatus = 'OF',
				c.createdTime = :now,
				c.lastConnectionTime = :now
			WHERE c.chatroomId = :id
				AND c.userId != :userId
			""")
	void updateReceiverConnectionStatus(@Param("id") long id,
										@Param("userId") long userId,
										@Param("now") Date now);
}
