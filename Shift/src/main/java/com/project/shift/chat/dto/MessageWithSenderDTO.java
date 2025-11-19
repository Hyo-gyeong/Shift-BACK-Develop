package com.project.shift.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewChatroomDTO {

    private ChatroomDTO chatroom;
    private MessageDTO message;
    private ChatroomUserDTO sender;
    private ChatroomUserDTO receiver;
    
}
