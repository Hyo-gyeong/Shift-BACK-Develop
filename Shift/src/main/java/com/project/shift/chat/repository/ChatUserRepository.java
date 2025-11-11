package com.project.shift.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.shift.chat.entity.ChatUserEntity;

public interface ChatUserRepository extends JpaRepository<ChatUserEntity, Long>{

	ChatUserEntity findByUserId(int userId);

	@Query("""
			SELECT u FROM ChatUserEntity u
            WHERE u.userId IN :friendIds
            ORDER BY u.name ASC
			""")
     List<ChatUserEntity> findUserInfoByIds(@Param("friendIds") List<Integer> friendIds);

	@Query("""
		    SELECT u FROM ChatUserEntity u
		    WHERE (:phone LIKE '%-%' AND u.phone = :phone)
		       OR (:phone NOT LIKE '%-%' AND REPLACE(u.phone, '-', '') = :phone)
			""") // 하이픈을 포함해서 입력하면 그대로 비교하고 하이픈을 포함하지 않고 검색하면 모든 숫자가 일치하는 결과값 반환 
	ChatUserEntity findByPhoneFlexible(@Param("phone") String phone);


}