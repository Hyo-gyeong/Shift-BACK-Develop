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
	@Query("UPDATE ChatroomUserEntity c "
			+ "SET c.connectionStatus = :status, c.lastConnectionTime = :time "
			+ "WHERE c.chatroomUsersId = :id")
	void updateChatUserInfo(@Param("status") String status,
							@Param("time") Date time,
							@Param("id") long id);
	
	// 특정 채팅방 유저 정보 반환
	@Query("""
			SELECT c FROM ChatroomUserEntity c
			WHERE c.chatroomId = :chatroomId AND c.userId = :userId
			""")
	Optional<ChatroomUserEntity> getChatroomUser(@Param("chatroomId") long chatroomId,
											  	 @Param("userId") long userId);
	
	// 채팅방 삭제 여부와 관계 없이 두 유저간 생성된 채팅방
	// not null  : 한 번이라도 같이 채팅을 한 적이 있음
	// null : 한 번도 같이 채팅을 한 적이 없음
	@Query("""
		    SELECT c.chatroomId
		    FROM ChatroomUserEntity c
		    WHERE c.userId IN :userIds
		    GROUP BY c.chatroomId
		    HAVING COUNT(DISTINCT c.userId) = :userCount
		""")
	Optional<Long> findChatroomWithUsers(@Param("userIds") List<Long> userIds,
										  @Param("userCount") long userCount);

}
