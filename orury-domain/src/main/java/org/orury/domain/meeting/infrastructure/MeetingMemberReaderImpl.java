package org.orury.domain.meeting.infrastructure;

import lombok.RequiredArgsConstructor;
import org.orury.domain.meeting.domain.MeetingMemberReader;
import org.orury.domain.meeting.domain.entity.MeetingMember;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MeetingMemberReaderImpl implements MeetingMemberReader {
    private final MeetingMemberRepository meetingMemberRepository;

    @Override
    public boolean existsByMeetingIdAndUserId(Long meetingId, Long userId) {
        return meetingMemberRepository.existsByMeetingMemberPK_MeetingIdAndMeetingMemberPK_UserId(meetingId, userId);
    }

    @Override
    public List<MeetingMember> getOtherMeetingMembersByMeetingIdMaximum(Long meetingId, Long meetingCreatorId, int maximum) {
        return meetingMemberRepository.findByMeetingMemberPK_MeetingIdAndMeetingMemberPK_UserIdNot(meetingId, meetingCreatorId, PageRequest.of(0, maximum)).toList();
    }

    @Override
    public List<MeetingMember> getMeetingMembersByMeetingId(Long meetingId) {
        return meetingMemberRepository.findByMeetingMemberPK_MeetingId(meetingId);
    }
}
