package com.project.shift.chat.dto;

public interface ChatroomListProjection {
	Long getChatroomUserId();
    Long getChatroomId();
    String getChatroomName();
    String getLastMsgContent();

    java.sql.Timestamp getLastMsgDate();
    java.sql.Timestamp getLastConnectionTime();
    java.sql.Timestamp getCreatedTime();

    String getConnectionStatus();
    String getIsDarkMode();
}
