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
                .connectionStatus(entity.getConnectionStatus())
                .isDarkMode(entity.getIsDarkMode())
                .build();
    }
    
}
