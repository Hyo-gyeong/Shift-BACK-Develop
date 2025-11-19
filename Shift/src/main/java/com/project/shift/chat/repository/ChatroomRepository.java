package com.project.shift.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
			from chatrooms c, chatroom_users cu
			where c.chatroom_id = cu.chatroom_id
				and cu.user_id = :userId
			""", nativeQuery = true)
	List<ChatroomListProjection> findChatroomsByUserId(@Param("userId") long userId);

}
