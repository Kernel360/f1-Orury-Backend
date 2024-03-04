package org.orury.domain.crew.domain.entity;

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
public class CrewMeetingMemberPK implements Serializable {
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "meeting_id", nullable = false)
    private Long meetingId;

    private CrewMeetingMemberPK(Long userId, Long meetingId) {
        this.userId = userId;
        this.meetingId = meetingId;
    }

    public static CrewMeetingMemberPK of(Long userId, Long meetingId) {
        return new CrewMeetingMemberPK(userId, meetingId);
    }
}
