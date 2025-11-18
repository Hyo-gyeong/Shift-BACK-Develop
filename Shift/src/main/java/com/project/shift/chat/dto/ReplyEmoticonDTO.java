package com.project.shift.chat.dto;

import com.project.shift.chat.entity.ReplyEmoticonEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReplyEmoticonDTO {
	
	private long replyEmoticonId;
	private long messageId;
	private long userId;
	private String type;

	// Entity -> DTO 변환
    public static ReplyEmoticonDTO toDto(ReplyEmoticonEntity entity) {
        return ReplyEmoticonDTO.builder()
                .replyEmoticonId(entity.getReplyEmoticonId())
                .messageId(entity.getMessageId())
                .userId(entity.getUserId())
                .type(entity.getType())
                .build();
    }
}
