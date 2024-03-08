package org.orury.domain.meeting.domain.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Slf4j
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"meetingMemberPK"}, callSuper = false)
@EntityListeners(AuditingEntityListener.class)
@Entity(name = "crew_meeting_member")
public class MeetingMember {
    @EmbeddedId
    private MeetingMemberPK meetingMemberPK;

    private MeetingMember(MeetingMemberPK meetingMemberPK) {
        this.meetingMemberPK = meetingMemberPK;
    }

    public static MeetingMember of(MeetingMemberPK meetingMemberPK) {
        return new MeetingMember(meetingMemberPK);
    }
}
