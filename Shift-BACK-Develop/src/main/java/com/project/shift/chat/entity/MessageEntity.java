package com.project.shift.chat.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.shift.chat.dto.MessageDTO;

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
@Table(name="MESSAGES")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageEntity {
    
	@Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "SEQ_MESSAGES"
    )
    @SequenceGenerator(
        name = "SEQ_MESSAGES",
        sequenceName = "SEQ_MESSAGES",
        allocationSize = 1
    )
    @Column(name = "MESSAGE_ID", nullable = false)
    private long messageId;

	// ✅ 연관관계 추가 - Chatroom
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CHATROOM_ID", nullable = false)
    @JsonIgnore // 양방향 관계 때문에 순환참조가 발생해 stackoverflow발생(특히 websocket에서) -> 직렬화에서 제외
    // dto를 사용하기 때문에 없어도 무방하지만 안전장치로서의 역할, 나중에 제거하는 것은 쉬움 (추가하는 것보다), 성능 오버헤드 전혀 없음
    private ChatroomEntity chatroom;
    
    // ✅ 연관관계 추가 - User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    @JsonIgnore
    private ChatUserEntity user;

    @Column(name = "SEND_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sendDate;

    @Column(name = "CONTENT", nullable = false, length = 300)
    private String content;
    
    @Column(name = "IS_GIFT", nullable = false, length = 1, columnDefinition = "CHAR(1) default 'N'")
    private String isGift;
    
    @Column(name = "UNREAD_COUNT", nullable = false)
    private int unreadCount;
    
// // ✅ 양방향 매핑 - 이 메시지에 달린 이모티콘들
//    아직 사용하지 않으므로 주석처리
//    @OneToMany(mappedBy = "message", fetch = FetchType.LAZY)
//    @JsonIgnore
//    @Builder.Default
//    private List<ReplyEmoticonEntity> replyEmoticons = new ArrayList<>();

    // DTO -> Entity 변환
    public static MessageEntity toEntity(MessageDTO dto) {
        return MessageEntity.builder()
                .messageId(dto.getMessageId())
                // chatroom과 user는 별도로 설정 필요
                .sendDate(dto.getSendDate())
                .content(dto.getContent())
                .isGift(dto.getIsGift())
                .unreadCount(dto.getUnreadCount())
                .build();
    }
    
    // ✅ 편의 메서드 추가
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