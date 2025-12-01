package com.project.shift.chat.dto;

public interface MessageSearchResultProjection {

	Long getChatroomUserId();
    Long getChatroomId();
    String getChatroomName();

    java.sql.Timestamp getLastConnectionTime();
    java.sql.Timestamp getCreatedTime();

    String getConnectionStatus();
    String getIsDarkMode();
    String getMessage();
}
