package com.project.shift.chat.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.shift.chat.dto.FriendDTO;

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
@Table(name = "FRIENDS")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendEntity {

    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "SEQ_FRIENDS"
    )
    @SequenceGenerator(
        name = "SEQ_FRIENDS",
        sequenceName = "SEQ_FRIENDS",
        allocationSize = 1
    )
    @Column(name = "FRIENDSHIP_ID")
    private long friendshipId;

    // ✅ 연관관계 추가 - User (친구 요청자)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    @JsonIgnore
    private ChatUserEntity user;

    // ✅ 연관관계 추가 - Friend (친구)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FRIEND_ID", nullable = false)
    @JsonIgnore
    private ChatUserEntity friend;

    // DTO → Entity 변환
    public static FriendEntity toEntity(FriendDTO dto) {
        return FriendEntity.builder()
                .friendshipId(dto.getFriendshipId())
                // user와 friend는 별도로 설정 필요
                .build();
    }
    
    // ✅ 편의 메서드
    public void setUser(ChatUserEntity user) {
        this.user = user;
    }
    
    public void setFriend(ChatUserEntity friend) {
        this.friend = friend;
    }
    
    // ✅ 기존 코드 호환용 getter
    public Long getUserId() {
        return user != null ? user.getUserId() : null;
    }
    
    public Long getFriendId() {
        return friend != null ? friend.getUserId() : null;
    }
}
