package com.project.shift.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeletedChatroomUserInfoDTO {

	private long chatroomId;
	private long userId;
	private long receiverId;
	private String userName;
	private String receiverName;
}
