package org.orury.domain.crew.infrastructures;

import org.orury.domain.crew.domain.entity.CrewMeeting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrewMeetingRepository extends JpaRepository<CrewMeeting, Long> {
}
