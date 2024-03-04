package org.orury.domain.crew.domain.dto;

import org.orury.domain.crew.domain.entity.CrewMeetingMember;
import org.orury.domain.crew.domain.entity.CrewMeetingMemberPK;

/**
 * DTO for {@link CrewMeetingMember}
 */
public record CrewMeetingMemberDto(
        CrewMeetingMemberPK crewMeetingMemberPK
) {
    public static CrewMeetingMemberDto of(CrewMeetingMemberPK crewMeetingMemberPK) {
        return new CrewMeetingMemberDto(crewMeetingMemberPK);
    }

    public static CrewMeetingMemberDto from(CrewMeetingMember crewMeetingMember) {
        return CrewMeetingMemberDto.of(crewMeetingMember.getCrewMeetingMemberPK());
    }

    public CrewMeetingMember toEntity() {
        return CrewMeetingMember.of(crewMeetingMemberPK);
    }
}
