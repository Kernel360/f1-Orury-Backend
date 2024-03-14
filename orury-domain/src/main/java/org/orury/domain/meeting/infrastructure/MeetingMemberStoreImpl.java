package org.orury.domain.meeting.infrastructure;

import lombok.RequiredArgsConstructor;
import org.orury.domain.meeting.domain.MeetingMemberStore;
import org.orury.domain.meeting.domain.dto.MeetingMemberDto;
import org.orury.domain.meeting.domain.entity.Meeting;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MeetingMemberStoreImpl implements MeetingMemberStore {
    private final MeetingMemberRepository meetingMemberRepository;
    private final MeetingRepository meetingRepository;

    @Override
    public void addMember(MeetingMemberDto meetingMemberDto) {
        meetingMemberRepository.save(meetingMemberDto.toEntity());
        meetingRepository.increaseMemberCount(meetingMemberDto.meetingMemberPK().getMeetingId());
    }

    @Override
    public void removeMember(MeetingMemberDto meetingMemberDto) {
        meetingMemberRepository.delete(meetingMemberDto.toEntity());
        meetingRepository.decreaseMemberCount(meetingMemberDto.meetingMemberPK().getMeetingId());
    }

    @Override
    public void removeAllByUserIdAndCrewId(Long userId, Long crewId) {
        List<Meeting> meetings = meetingRepository.findAllByCrew_Id(crewId);
        meetings.forEach(meeting -> meetingMemberRepository.findByMeetingMemberPK_MeetingIdAndMeetingMemberPK_UserId(meeting.getId(), userId)
                .ifPresent(meetingMember -> {
                    meetingMemberRepository.delete(meetingMember);
                    meetingRepository.decreaseMemberCount(meeting.getId());
                })
        );
    }
}
