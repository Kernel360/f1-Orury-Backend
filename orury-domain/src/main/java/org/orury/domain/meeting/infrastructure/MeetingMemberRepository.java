package org.orury.domain.meeting.infrastructure;

import org.orury.domain.meeting.domain.entity.MeetingMember;
import org.orury.domain.meeting.domain.entity.MeetingMemberPK;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MeetingMemberRepository extends JpaRepository<MeetingMember, MeetingMemberPK> {
    boolean existsByMeetingMemberPK_MeetingIdAndMeetingMemberPK_UserId(Long meetingId, Long userId);

    Optional<MeetingMember> findByMeetingMemberPK_MeetingIdAndMeetingMemberPK_UserId(Long meetingId, Long userId);

    List<MeetingMember> findByMeetingMemberPK_MeetingIdAndMeetingMemberPK_UserIdNot(Long meetingId, Long meetingCreatorId, PageRequest pageRequest);

    List<MeetingMember> findByMeetingMemberPK_MeetingId(Long meetingId);
}
