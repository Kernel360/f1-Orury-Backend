package org.orury.domain.crew.domain.entity;

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
@EqualsAndHashCode(of = {"crewMeetingMemberPK"}, callSuper = false)
@EntityListeners(AuditingEntityListener.class)
@Entity(name = "crew_meeting_member")
public class CrewMeetingMember {
    @EmbeddedId
    private CrewMeetingMemberPK crewMeetingMemberPK;

    private CrewMeetingMember(CrewMeetingMemberPK crewMeetingMemberPK) {
        this.crewMeetingMemberPK = crewMeetingMemberPK;
    }

    public static CrewMeetingMember of(CrewMeetingMemberPK crewMeetingMemberPK) {
        return new CrewMeetingMember(crewMeetingMemberPK);
    }
}
