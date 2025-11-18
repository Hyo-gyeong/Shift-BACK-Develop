package com.project.shift.chat.dto;

import java.util.Date;

import com.project.shift.chat.entity.MessageEntity;

import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDTO {

	public enum MessageType {
        CHAT, JOIN, LEAVE
    }
	@Transient //DB와 매핑하지 않는 필드
	private MessageType type;
	
    private long messageId;
    private long chatroomId;
    private long userId;
    private Date sendDate;
    private String content;
    private String isGift;
    private int unreadCount;

    // Entity -> DTO 변환
    public static MessageDTO toDto(MessageEntity entity) {
        return MessageDTO.builder()
                .messageId(entity.getMessageId())
                .chatroomId(entity.getChatroomId())
                .userId(entity.getUserId())
                .sendDate(entity.getSendDate())
                .content(entity.getContent())
                .isGift(entity.getIsGift())
                .unreadCount(entity.getUnreadCount())
                .build();
    }
    
    // 채팅방 생성시 사용할 생성자
    MessageDTO(long chatroomId, long userId, Date sendDate,
    			String content, String isGift, int unreadCount){
    	this.chatroomId = chatroomId;
    	this.userId = userId;
    	this.sendDate = sendDate;
    	this.content = content;
    	this.isGift = isGift;
    	this.unreadCount = unreadCount;
    }

}
