package org.orury.client.meeting.application;

import lombok.RequiredArgsConstructor;
import org.orury.common.error.code.CrewErrorCode;
import org.orury.common.error.code.MeetingErrorCode;
import org.orury.common.error.exception.BusinessException;
import org.orury.domain.crew.domain.CrewMemberReader;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.meeting.domain.MeetingMemberReader;
import org.orury.domain.meeting.domain.MeetingMemberStore;
import org.orury.domain.meeting.domain.MeetingReader;
import org.orury.domain.meeting.domain.MeetingStore;
import org.orury.domain.meeting.domain.dto.MeetingDto;
import org.orury.domain.meeting.domain.dto.MeetingMemberDto;
import org.orury.domain.meeting.domain.entity.Meeting;
import org.orury.domain.meeting.domain.entity.MeetingMember;
import org.orury.domain.meeting.domain.entity.MeetingMemberPK;
import org.orury.domain.user.domain.UserReader;
import org.orury.domain.user.domain.dto.UserDto;
import org.orury.domain.user.domain.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService {
    private final MeetingReader meetingReader;
    private final MeetingStore meetingStore;
    private final MeetingMemberReader meetingMemberReader;
    private final MeetingMemberStore meetingMemberStore;
    private final CrewMemberReader crewMemberReader;
    private final UserReader userReader;

    @Override
    @Transactional(readOnly = true)
    public MeetingDto getMeetingDtoById(Long meetingId) {
        Meeting meeting = meetingReader.findById(meetingId)
                .orElseThrow(() -> new BusinessException(MeetingErrorCode.NOT_FOUND));
        return MeetingDto.from(meeting);
    }

    @Override
    @Transactional
    public void createMeeting(MeetingDto meetingDto) {
        validateCrewMember(meetingDto.crewDto().id(), meetingDto.userDto().id());
        validateStartTime(meetingDto.startTime());
        validateCapacity(meetingDto.capacity(), meetingDto.crewDto().memberCount());
        Meeting meeting = meetingStore.createMeeting(meetingDto.toEntity());
        MeetingMemberDto meetingMemberDto = MeetingMemberDto.of(MeetingMemberPK.of(meetingDto.userDto().id(), meeting.getId()));
        meetingMemberStore.addMember(meetingMemberDto.toEntity());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MeetingDto> getPresentMeetingDtosByCrewId(Long crewId, Long userId) {
        validateCrewMember(crewId, userId);
        List<Meeting> meetings = meetingReader.getNotStartedMeetingsByCrewId(crewId);
        return convertMeetingsToMeetingDtos(meetings, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MeetingDto> getPastMeetingDtosByCrewId(Long crewId, Long userId) {
        validateCrewMember(crewId, userId);
        List<Meeting> meetings = meetingReader.getStartedMeetingsByCrewId(crewId);
        return convertMeetingsToMeetingDtos(meetings, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getUserImagesByMeeting(MeetingDto meetingDto) {
        UserDto meetingCreator = meetingDto.userDto();
        List<MeetingMember> otherMembers = meetingMemberReader.getOtherMeetingMembersByMeetingIdMaximum(meetingDto.id(), meetingDto.userDto().id(), NumberConstants.MAXIMUM_OF_MEETING_THUMBNAILS - 1);

        List<String> userImages = otherMembers.stream()
                .map(meetingMember -> meetingMember.getMeetingMemberPK().getUserId())
                .map(userReader::getUserById)
                .map(User::getProfileImage)
                .collect(Collectors.toCollection(LinkedList::new));
        userImages.add(0, meetingCreator.profileImage());
        return userImages;
    }

    @Override
    @Transactional
    public void updateMeeting(MeetingDto meetingDto, Long userId) {
        validateMeetingCreator(meetingDto.userDto().id(), userId);
        validateStartTime(meetingDto.startTime());
        validateCapacity(meetingDto.capacity(), meetingDto.crewDto().memberCount(), meetingDto.memberCount());
        meetingStore.updateMeeting(meetingDto.toEntity());
    }

    @Override
    @Transactional
    public void deleteMeeting(MeetingDto meetingDto, Long userId) {
        validateMeetingCreator(meetingDto.userDto().id(), userId);
        meetingStore.deleteMeeting(meetingDto.id());
    }

    @Override
    @Transactional
    public void addMeetingMember(MeetingDto meetingDto, Long userId) {
        validateCrewMember(meetingDto.crewDto().id(), userId);
        if (meetingMemberReader.existsByMeetingIdAndUserId(meetingDto.id(), userId))
            throw new BusinessException(MeetingErrorCode.ALREADY_JOINED_MEETING);
        if (meetingDto.memberCount() >= meetingDto.capacity())
            throw new BusinessException(MeetingErrorCode.FULL_MEETING);
        MeetingMemberDto meetingMemberDto = MeetingMemberDto.of(MeetingMemberPK.of(userId, meetingDto.id()));
        meetingMemberStore.addMember(meetingMemberDto.toEntity());
    }

    @Override
    @Transactional
    public void removeMeetingMember(MeetingDto meetingDto, Long userId) {
        validateCrewMember(meetingDto.crewDto().id(), userId);
        if (Objects.equals(meetingDto.userDto().id(), userId))
            throw new BusinessException(MeetingErrorCode.MEETING_CREATOR);
        if (!meetingMemberReader.existsByMeetingIdAndUserId(meetingDto.id(), userId))
            throw new BusinessException(MeetingErrorCode.NOT_JOINED_MEETING);
        MeetingMemberDto meetingMemberDto = MeetingMemberDto.of(MeetingMemberPK.of(userId, meetingDto.id()));
        meetingMemberStore.removeMember(meetingMemberDto.toEntity());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUserDtosByMeeting(MeetingDto meetingDto, Long userId) {
        validateCrewMember(meetingDto.crewDto().id(), userId);
        List<MeetingMember> meetingMembers = meetingMemberReader.getMeetingMembersByMeetingId(meetingDto.id());
        return convertMeetingMembersToUserDtos(meetingMembers);
    }

    private void validateMeetingCreator(Long meetingCreatorId, Long userId) {
        if (!Objects.equals(meetingCreatorId, userId))
            throw new BusinessException(MeetingErrorCode.FORBIDDEN);
    }

    private void validateCrewMember(Long crewId, Long userId) {
        if (!crewMemberReader.existsByCrewIdAndUserId(crewId, userId))
            throw new BusinessException(CrewErrorCode.NOT_CREW_MEMBER);
    }

    private void validateStartTime(LocalDateTime startTime) {
        if (LocalDateTime.now().isAfter(startTime))
            throw new BusinessException(MeetingErrorCode.INVALID_START_TIME);
    }

    private void validateCapacity(int capacity, int crewMemberCount) {
        if (capacity < NumberConstants.MINIMUM_MEETING_CAPACITY || crewMemberCount < capacity)
            throw new BusinessException(MeetingErrorCode.INVALID_CAPACITY);
    }

    private void validateCapacity(int capacity, int crewMemberCount, int meetingMemberCount) {
        if (capacity < NumberConstants.MINIMUM_MEETING_CAPACITY || crewMemberCount < capacity)
            throw new BusinessException(MeetingErrorCode.INVALID_CAPACITY);
        if (capacity < meetingMemberCount)
            throw new BusinessException(MeetingErrorCode.CAPACITY_FORBIDDEN);
    }

    private List<MeetingDto> convertMeetingsToMeetingDtos(List<Meeting> meetings, Long userId) {
        return meetings.stream().map(meeting -> {
            boolean isParticipated = meetingMemberReader.existsByMeetingIdAndUserId(meeting.getId(), userId);
            return MeetingDto.from(meeting, isParticipated);
        }).toList();
    }

    private List<UserDto> convertMeetingMembersToUserDtos(List<MeetingMember> meetingMembers) {
        return meetingMembers.stream().map(meetingMember -> {
            User user = userReader.getUserById(meetingMember.getMeetingMemberPK().getUserId());
            return UserDto.from(user);
        }).toList();
    }
}
