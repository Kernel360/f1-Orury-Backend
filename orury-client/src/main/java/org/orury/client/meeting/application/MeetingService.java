package org.orury.client.meeting.application;

import org.orury.domain.meeting.domain.dto.MeetingDto;
import org.orury.domain.user.domain.dto.UserDto;

import java.util.List;

public interface MeetingService {
    MeetingDto getMeetingDtoById(Long meetingId);

    void createMeeting(MeetingDto meetingDto);

    List<MeetingDto> getPresentMeetingDtosByCrewId(Long crewId, Long userId);

    List<MeetingDto> getPastMeetingDtosByCrewId(Long crewId, Long userId);

    List<String> getUserImagesByMeeting(MeetingDto meetingDto);

    void updateMeeting(MeetingDto meetingDto, Long userId);

    void deleteMeeting(MeetingDto meetingDto, Long userId);

    void addMeetingMember(MeetingDto meetingDto, Long userId);

    void removeMeetingMember(MeetingDto meetingDto, Long userId);

    List<UserDto> getUserDtosByMeeting(MeetingDto meetingDto, Long userId);
}
