package com.project.shift.chat.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.shift.chat.dto.ChatroomUserDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    @Column(name = "CHATROOM_USERS_ID", nullable = false)
    private long chatroomUserId;
    
    // ✅ 연관관계 추가 - Chatroom
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CHATROOM_ID", nullable = false)
    @JsonIgnore
    private ChatroomEntity chatroom;
    
    // ✅ 연관관계 추가 - User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    @JsonIgnore
    private ChatUserEntity user;

    @Column(name = "CHATROOM_NAME", length = 30)
    private String chatroomName;

    @Column(name = "LAST_CONNECTION_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastConnectionTime;
    
    @Column(name = "CREATED_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    @Column(name = "CONNECTION_STATUS", nullable = false, length = 2)
    private String connectionStatus;

    @Column(name = "IS_DARK_MODE", nullable = false, length = 1, columnDefinition = "CHAR(1) default 'N'")
    private String isDarkMode;
    
    // DTO -> Entity 변환
    public static ChatroomUserEntity toEntity(ChatroomUserDTO dto) {
        return ChatroomUserEntity.builder()
        		.chatroomUserId(dto.getChatroomUserId())
                .chatroomName(dto.getChatroomName())
                .lastConnectionTime(dto.getLastConnectionTime())
                .createdTime(dto.getCreatedTime())
                .connectionStatus(dto.getConnectionStatus())
                .isDarkMode(dto.getIsDarkMode())
                .build();
    }

    // ✅ 편의 메서드
    public void setChatroom(ChatroomEntity chatroom) {
        this.chatroom = chatroom;
    }
    
    public void setUser(ChatUserEntity user) {
        this.user = user;
    }
    
    // ✅ 기존 코드 호환용 getter
    public Long getChatroomId() {
        return chatroom != null ? chatroom.getChatroomId() : null;
    }
    
    public Long getUserId() {
        return user != null ? user.getUserId() : null;
    }
}