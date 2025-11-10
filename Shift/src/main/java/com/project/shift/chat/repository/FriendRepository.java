package com.project.shift.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.shift.chat.entity.FriendEntity;

import jakarta.transaction.Transactional;

public interface FriendRepository extends JpaRepository<FriendEntity, Long>{
	
    List<FriendEntity> findByUserId(int userId);
    
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO FRIENDS VALUES (SEQ_FRIENDS.NEXTVAL, :userId, :friendId)", nativeQuery = true)
    void insertFriend(@Param("userId") int userId, @Param("friendId") int friendId);
}
