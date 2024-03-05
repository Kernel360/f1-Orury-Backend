package org.orury.domain.meeting.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class MeetingMemberPK implements Serializable {
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "meeting_id", nullable = false)
    private Long meetingId;

    private MeetingMemberPK(Long userId, Long meetingId) {
        this.userId = userId;
        this.meetingId = meetingId;
    }

    public static MeetingMemberPK of(Long userId, Long meetingId) {
        return new MeetingMemberPK(userId, meetingId);
    }
}
