package com.project.shift.chat.entity;

import com.project.shift.chat.dto.ReplyEmoticonDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    @Column(name = "REPLY_EMOTICON_ID")
    private long replyEmoticonId;

    @Column(name = "MESSAGE_ID")
    private long messageId;

    @Column(name = "USER_ID")
    private long userId;
    
    @Column(name = "TYPE", length = 1, columnDefinition = "CHAR(3)")
    private String type;
    
    // DTO -> Entity 변환
    public static ReplyEmoticonEntity toEntity(ReplyEmoticonDTO dto) {
        return ReplyEmoticonEntity.builder()
                .replyEmoticonId(dto.getReplyEmoticonId())
                .messageId(dto.getMessageId())
                .userId(dto.getUserId())
                .type(dto.getType())
                .build();
    }

}