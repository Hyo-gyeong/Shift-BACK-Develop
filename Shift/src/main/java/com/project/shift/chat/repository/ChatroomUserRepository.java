package com.project.shift.chat.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.project.shift.chat.entity.ChatroomUserEntity;

public interface ChatroomUserRepository extends JpaRepository<ChatroomUserEntity, Long>{

	@Modifying
	@Transactional
	@Query("UPDATE ChatroomUserEntity c "
			+ "SET c.connectionStatus = :status, c.lastConnectionTime = :time "
			+ "WHERE c.chatroomUsersId = :id")
	void updateChatUserInfo(@Param("status") String status,
							@Param("time") Date time,
							@Param("id") long id);
}
