package com.project.shift.chat.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.project.shift.chat.entity.ChatroomEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatroomDTO {

    private long chatroomId;
    @Setter
    private long fromUserId;
    @Setter
    private long toUserId;
    @Setter
    private String chatroomName;
    @Setter
    private String connectionStatus; // 'ON' 또는 'OF'
    @Setter
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date connectionTime;
    @Setter
    private String isDarkMode; // 'Y' 또는 'N'

    // Entity -> DTO 변환
    public static ChatroomDTO toDto(ChatroomEntity entity) {
        return ChatroomDTO.builder()
                .chatroomId(entity.getChatroomId())
                .fromUserId(entity.getFromUserId())
                .toUserId(entity.getToUserId())
                .chatroomName(entity.getChatroomName())
                .connectionStatus(entity.getConnectionStatus())
                .connectionTime(entity.getConnectionTime())
                .isDarkMode(entity.getIsDarkMode())
                .build();
    }
    
}
