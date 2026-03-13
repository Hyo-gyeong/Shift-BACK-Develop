package com.project.shift.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.shift.chat.entity.ChatUserEntity;

public interface ChatUserRepository extends JpaRepository<ChatUserEntity, Long>{

	// userId로 친구 검색
	@Query("""
			SELECT u FROM ChatUserEntity u
            WHERE u.userId IN :friendIds
            ORDER BY u.name ASC
			""")
     List<ChatUserEntity> findUserInfoByIds(@Param("friendIds") List<Integer> friendIds);

	// 하이픈 없이 정확하게 일치하는 핸드폰 번호로만 친구 검색
	@Query("""
		    SELECT u FROM ChatUserEntity u
		    WHERE u.phone = :phone
			""") 
	ChatUserEntity findByPhoneFlexible(@Param("phone") String phone);

}
