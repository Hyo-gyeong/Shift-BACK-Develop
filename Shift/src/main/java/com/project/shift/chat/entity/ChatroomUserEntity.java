package com.project.shift.chat.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.project.shift.chat.dto.ChatroomUserDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="CHATROOM_USERS")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatroomUserEntity {

    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "SEQ_CHATROOM_USERS"
    )
    @SequenceGenerator(
        name = "SEQ_CHATROOM_USERS",
        sequenceName = "SEQ_CHATROOM_USERS",
        allocationSize = 1
    )
    @Column(name = "CHATROOM_USERS_ID")
    private long chatroomUserId;
    
    @Column(name = "CHATROOM_ID")
    private long chatroomId;
    
    @Column(name = "USER_ID")
    private long userId;

    @Column(name = "CHATROOM_NAME", length = 30, columnDefinition = "CHAR(30)")
    private String chatroomName;

    @Column(name = "LAST_CONNECTION_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastConnectionTime;

    @Column(name = "CONNECTION_STATUS", length = 2, columnDefinition = "CHAR(2)")
    private String connectionStatus;

    @Column(name = "IS_DARK_MODE", length = 1, columnDefinition = "CHAR(1) default 'N'")
    private String isDarkMode;
    
    // DTO -> Entity 변환
    public static ChatroomUserEntity toEntity(ChatroomUserDTO dto) {
        return ChatroomUserEntity.builder()
                .chatroomUserId(dto.getChatroomId())
                .chatroomId(dto.getChatroomId())
                .userId(dto.getUserId())
                .chatroomName(dto.getChatroomName())
                .lastConnectionTime(dto.getLastConnectionTime())
                .connectionStatus(dto.getConnectionStatus())
                .isDarkMode(dto.getIsDarkMode())
                .build();
    }

}