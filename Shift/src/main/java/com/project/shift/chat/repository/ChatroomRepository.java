package com.project.shift.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.project.shift.chat.dto.ChatroomListProjection;
import com.project.shift.chat.entity.ChatroomEntity;

public interface ChatroomRepository extends JpaRepository<ChatroomEntity, Long>{

	// 채팅방 목록 조회
	@Query(value = """
			select
				cu.chatroom_users_id as chatroomUsersId,
				cu.chatroom_id as chatroomId,
				cu.chatroom_name as chatroomName,
				cu.last_connection_time as lastConnectionTime,
				cu.connection_status as connectionStatus,
				cu.created_time as createdTime,
				cu.is_dark_mode as isDarkMode,
				c.last_msg_content as lastMsgContent,
				c.last_msg_date as lastMsgDate
			from chatroom_users cu 
			join chatrooms c ON c.chatroom_id = cu.chatroom_id
			where cu.user_id = :userId 
				and cu.connection_status != 'DL'
			""", nativeQuery = true)
	List<ChatroomListProjection> findChatroomsByUserId(@Param("userId") long userId);

	// 채팅 삭제 → 키값 빼고 초기화
	@Modifying
	@Transactional
	@Query("""
			UPDATE ChatroomEntity c 
			SET c.lastMsgContent = null,
				   c.lastMsgDate = null 
			WHERE c.chatroomId = :chatroomId
			""")
	void initChatroomExceptKey(@Param("chatroomId") long chatroomId);
	
}
