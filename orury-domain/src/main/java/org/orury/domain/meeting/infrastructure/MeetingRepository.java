package org.orury.domain.meeting.infrastructure;

import org.orury.domain.meeting.domain.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
}
