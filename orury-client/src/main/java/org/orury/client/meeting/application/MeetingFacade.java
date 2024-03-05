package org.orury.client.meeting.application;

import lombok.RequiredArgsConstructor;
import org.orury.client.crew.application.CrewService;
import org.orury.client.gym.application.GymService;
import org.orury.client.meeting.interfaces.request.MeetingCreateRequest;
import org.orury.client.meeting.interfaces.request.MeetingUpdateRequest;
import org.orury.client.meeting.interfaces.response.MeetingMembersResponse;
import org.orury.client.meeting.interfaces.response.MeetingsResponse;
import org.orury.client.user.application.UserService;
import org.orury.domain.crew.domain.dto.CrewDto;
import org.orury.domain.gym.domain.dto.GymDto;
import org.orury.domain.meeting.domain.dto.MeetingDto;
import org.orury.domain.user.domain.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingFacade {
    private final MeetingService meetingService;
    private final CrewService crewService;
    private final UserService userService;
    private final GymService gymService;

    public void createMeeting(MeetingCreateRequest request, Long userId) {
        UserDto userDto = userService.getUserDtoById(userId);
        GymDto gymDto = gymService.getGymDtoById(request.gymId());
        CrewDto crewDto = crewService.getCrewDtoById(request.crewId());

        MeetingDto meetingDto = request.toDto(userDto, gymDto, crewDto);
        meetingService.createMeeting(meetingDto);
    }

    public List<MeetingsResponse> getPresentMeetings(Long crewId, Long userId) {
        List<MeetingDto> meetingDtos = meetingService.getPresentMeetingDtosByCrewId(crewId, userId);
        return meetingDtos.stream().map(meetingDto -> {
            List<String> userImages = meetingService.getUserImagesByMeetingId(meetingDto.id());
            return MeetingsResponse.of(meetingDto, userImages, userId);
        }).toList();
    }

    public List<MeetingsResponse> getPastMeetings(Long crewId, Long userId) {
        List<MeetingDto> meetingDtos = meetingService.getPastMeetingDtosByCrewId(crewId, userId);
        return meetingDtos.stream().map(meetingDto -> {
            List<String> userImages = meetingService.getUserImagesByMeetingId(meetingDto.id());
            return MeetingsResponse.of(meetingDto, userImages, userId);
        }).toList();
    }

    public void updateMeeting(MeetingUpdateRequest request, Long userId) {
        MeetingDto originalMeetingDto = meetingService.getMeetingDtoById(request.meetingId());
        GymDto newGymDto = gymService.getGymDtoById(request.gymId());
        MeetingDto newMeetingDto = request.toDto(originalMeetingDto, newGymDto);
        meetingService.updateMeeting(newMeetingDto, userId);
    }

    public void deleteMeeting(Long meetingId, Long userId) {
        MeetingDto meetingDto = meetingService.getMeetingDtoById(meetingId);
        meetingService.deleteMeeting(meetingDto, userId);
    }

    public void addMeetingMember(Long meetingId, Long userId) {
        MeetingDto meetingDto = meetingService.getMeetingDtoById(meetingId);
        meetingService.addMeetingMember(meetingDto, userId);
    }

    public void removeMeetingMember(Long meetingId, Long userId) {
        MeetingDto meetingDto = meetingService.getMeetingDtoById(meetingId);
        meetingService.removeMeetingMember(meetingDto, userId);
    }

    public List<MeetingMembersResponse> getMeetingMembers(Long meetingId, Long userId) {
        MeetingDto meetingDto = meetingService.getMeetingDtoById(meetingId);
        List<UserDto> userDtos = meetingService.getUserDtosByMeeting(meetingDto);
        return userDtos.stream()
                .map(userDto -> MeetingMembersResponse.of(userDto, userId, meetingDto.userDto().id()))
                .toList();
    }
}
