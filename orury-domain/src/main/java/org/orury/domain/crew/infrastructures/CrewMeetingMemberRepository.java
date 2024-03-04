package org.orury.domain.crew.infrastructures;

import org.orury.domain.crew.domain.entity.CrewMeetingMember;
import org.orury.domain.crew.domain.entity.CrewMeetingMemberPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrewMeetingMemberRepository extends JpaRepository<CrewMeetingMember, CrewMeetingMemberPK> {
}
