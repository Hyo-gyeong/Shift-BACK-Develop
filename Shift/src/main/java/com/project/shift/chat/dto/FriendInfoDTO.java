package com.project.shift.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendInfoDTO {

    private long friendshipId; // 친구 관계 테이블 PK
    private long friendId;     // PK
    private String friendName; // 이름
    private String friendUserID; // ID(로그인에 사용)
}
