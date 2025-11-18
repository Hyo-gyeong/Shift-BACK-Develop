package com.project.shift.chat.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.project.shift.chat.entity.ChatroomUserEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatroomUserDTO {

    private long chatroomUserId;
    private long chatroomId;
    private long userId;
    private String chatroomName;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastConnectionTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
    private String connectionStatus;
    private String isDarkMode;
    
    // Entity -> DTO 변환
    public static ChatroomUserDTO toDto(ChatroomUserEntity entity) {
        return ChatroomUserDTO.builder()
                .chatroomUserId(entity.getChatroomId())
                .chatroomId(entity.getChatroomId())
                .userId(entity.getUserId())
                .chatroomName(entity.getChatroomName())
                .lastConnectionTime(entity.getLastConnectionTime())
                .createdTime(entity.getCreatedTime())
                .connectionStatus(entity.getConnectionStatus())
                .isDarkMode(entity.getIsDarkMode())
                .build();
    }
    
    // 채팅방 생성시 사용할 생성자
    ChatroomUserDTO(long userId, String chatroomName,
    				String connectionStatus, String isDarkMode){
        this.userId = userId;
        this.chatroomName = chatroomName;
        this.connectionStatus = connectionStatus;
        this.isDarkMode = isDarkMode;
    }
    
}
