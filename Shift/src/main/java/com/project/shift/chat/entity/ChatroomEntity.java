package com.project.shift.chat.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Chatrooms")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Chatroom {

    @Id
    @Column(name = "CHATROOM_ID")
    private int chatroomId;

    @Column(name = "FROM_USER_ID")
    private int fromUserId;

    @Column(name = "TO_USER_ID")
    private int toUserId;

    @Column(name = "CHATROOM_NAME")
    private String chatroomName;

    @Column(name = "CONNECTION_STATUS", length = 2)
    private String connectionStatus; // 'ON' 또는 'OF'

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "CONNECTION_TIME")
    private Date connectionTime;

    @Column(name = "IS_DARK_MODE", length = 1)
    private String isDarkMode; // DEFAULT 'N', 'Y' 또는 'N'
}
