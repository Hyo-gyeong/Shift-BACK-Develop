package com.project.shift.chat.dto;

import java.util.Date;

import com.project.shift.chat.entity.MessageEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDTO {

    private long messageId;
    private long chatroomId;
    private String isFromUser;
    private String isRead; // DEFAULT 'N', 'Y' 또는 'N'
    private Date sentDate;
    private String messageContent;
    private String isGift; // DEFAULT 'N', 'Y' 또는 'N'
    private String isEmoji; // DEFAULT 'N', 'Y' 또는 'N'

    // Entity -> DTO 변환
    public static MessageDTO toDto(MessageEntity entity) {
        return MessageDTO.builder()
                .messageId(entity.getMessageId())
                .chatroomId(entity.getChatroomId())
                .isFromUser(entity.getIsFromUser())
                .isRead(entity.getIsRead())
                .sentDate(entity.getSentDate())
                .messageContent(entity.getMessageContent())
                .isGift(entity.getIsGift())
                .isEmoji(entity.getIsEmoji())
                .build();
    }

}
