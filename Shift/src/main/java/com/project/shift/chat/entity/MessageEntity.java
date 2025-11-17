package com.project.shift.chat.entity;

import java.util.Date;

import com.project.shift.chat.dto.MessageDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="MESSAGES")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageEntity {
    
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "SEQ_MESSAGES"
    )
    @SequenceGenerator(
        name = "SEQ_MESSAGES",
        sequenceName = "SEQ_MESSAGES",
        allocationSize = 1
    )
    
    @Id
    @Column(name = "MESSAGE_ID")
    private long messageId;

    @Column(name = "CHATROOM_ID")
    private long chatroomId;
    
    @Column(name = "USER_ID", nullable = false)
    private long userId;

    @Column(name = "SEND_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sendDate;

    @Column(name = "CONTENT", length = 300, columnDefinition = "CHAR(300)")
    private String content;
    
    @Column(name = "IS_GIFT", length = 1, columnDefinition = "CHAR(1) default 'N'")
    private String isGift;
    
    @Column(name = "UNREAD_COUNT")
    private int unreadCount;

    // DTO -> Entity 변환
    public static MessageEntity toEntity(MessageDTO dto) {
        return MessageEntity.builder()
                .messageId(dto.getMessageId())
                .chatroomId(dto.getChatroomId())
                .userId(dto.getUserId())
                .sendDate(dto.getSendDate())
                .content(dto.getContent())
                .isGift(dto.getIsGift())
                .unreadCount(dto.getUnreadCount())
                .build();
    }

}