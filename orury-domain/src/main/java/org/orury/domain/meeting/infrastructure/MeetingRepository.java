package org.orury.domain.meeting.infrastructure;

import org.orury.domain.meeting.domain.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    List<Meeting> findByCrew_IdAndStartTimeBeforeOrderByIdDesc(Long crewId, LocalDateTime startTime);

    List<Meeting> findByCrew_IdAndStartTimeAfterOrderByIdDesc(Long crewId, LocalDateTime startTime);

    @Modifying
    @Query("UPDATE crew_meeting SET memberCount = memberCount + 1 WHERE id = :meetingId")
    void increaseMemberCount(Long meetingId);

    @Modifying
    @Query("UPDATE crew_meeting SET memberCount = memberCount - 1 WHERE id = :meetingId")
    void decreaseMemberCount(Long meetingId);
}
