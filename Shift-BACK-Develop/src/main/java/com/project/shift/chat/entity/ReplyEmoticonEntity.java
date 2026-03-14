package com.project.shift.chat.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.shift.chat.dto.ReplyEmoticonDTO;

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
@Table(name="REPLY_EMOTICONS")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReplyEmoticonEntity {

	@Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "SEQ_REPLY_EMOTICONS"
    )
    @SequenceGenerator(
        name = "SEQ_REPLY_EMOTICONS",
        sequenceName = "SEQ_REPLY_EMOTICONS",
        allocationSize = 1
    )
    @Column(name = "REPLY_EMOTICON_ID", nullable = false)
    private long replyEmoticonId;

	// ✅ 연관관계 추가 - Message
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MESSAGE_ID", nullable = false)
    @JsonIgnore
    private MessageEntity message;

    // ✅ 연관관계 추가 - User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    @JsonIgnore
    private ChatUserEntity user;
    
    @Column(name = "TYPE", nullable = false, length = 3)
    private String type;
    
    // DTO -> Entity 변환
    public static ReplyEmoticonEntity toEntity(ReplyEmoticonDTO dto) {
        return ReplyEmoticonEntity.builder()
                .replyEmoticonId(dto.getReplyEmoticonId())
                // message와 user는 별도 설정
                .type(dto.getType())
                .build();
    }
    
    // ✅ 편의 메서드
    public void setMessage(MessageEntity message) {
        this.message = message;
    }
    
    public void setUser(ChatUserEntity user) {
        this.user = user;
    }
    
    // ✅ 기존 코드 호환용 getter
    public Long getMessageId() {
        return message != null ? message.getMessageId() : null;
    }
    
    public Long getUserId() {
        return user != null ? user.getUserId() : null;
    }

}