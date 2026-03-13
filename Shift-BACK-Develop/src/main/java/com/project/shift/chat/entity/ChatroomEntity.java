package com.project.shift.chat.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.shift.chat.dto.ChatroomDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="CHATROOMS")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatroomEntity {

    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "SEQ_CHATROOMS"
    )
    @SequenceGenerator(
        name = "SEQ_CHATROOMS",
        sequenceName = "SEQ_CHATROOMS",
        allocationSize = 1
    )
    @Column(name = "CHATROOM_ID", nullable = false)
    private Long chatroomId;

    @Column(name = "LAST_MSG_CONTENT")
    private String lastMsgContent;

    @Column(name = "LAST_MSG_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastMsgDate;
    
    // ✅ 양방향 매핑 - 이 채팅방의 메시지들
    @OneToMany(mappedBy = "chatroom", fetch = FetchType.LAZY)
    @JsonIgnore
    @Builder.Default // builder 설정에 없는 필드가 null이 아닌 빈 arraylist로 초기화 되도록 설정
    // NullPointerException 방지, 안전한 컬렉션 접근 보장, 코드 품질 향상
    private List<MessageEntity> messages = new ArrayList<>();
    
    // ✅ 양방향 매핑 - 이 채팅방의 참여자들
    @OneToMany(mappedBy = "chatroom", fetch = FetchType.LAZY)
    @JsonIgnore
    @Builder.Default
    private List<ChatroomUserEntity> chatroomUsers = new ArrayList<>();

    // DTO -> Entity 변환
    public static ChatroomEntity toEntity(ChatroomDTO dto) {
        return ChatroomEntity.builder()
                .lastMsgContent(dto.getLastMsgContent())
                .lastMsgDate(dto.getLastMsgDate())
                .build();
    }

}