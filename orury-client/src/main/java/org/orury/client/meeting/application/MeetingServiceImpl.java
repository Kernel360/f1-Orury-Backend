package org.orury.client.meeting.application;

import lombok.RequiredArgsConstructor;
import org.orury.common.error.code.CrewErrorCode;
import org.orury.common.error.code.MeetingErrorCode;
import org.orury.common.error.exception.BusinessException;
import org.orury.domain.crew.domain.CrewMemberReader;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.global.image.ImageReader;
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

import static org.orury.common.util.S3Folder.GYM;

@Service
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService {
    private final MeetingReader meetingReader;
    private final MeetingStore meetingStore;
    private final MeetingMemberReader meetingMemberReader;
    private final MeetingMemberStore meetingMemberStore;
    private final CrewMemberReader crewMemberReader;
    private final UserReader userReader;
    private final ImageReader imageReader;

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
        validateTimes(meetingDto.startTime(), meetingDto.endTime());
        Meeting meeting = meetingStore.createMeeting(meetingDto.toEntity());
        MeetingMemberDto meetingMemberDto = MeetingMemberDto.of(MeetingMemberPK.of(meetingDto.userDto().id(), meeting.getId()));
        meetingMemberStore.addMember(meetingMemberDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MeetingDto> getPresentMeetingDtosByCrewId(Long crewId, Long userId) {
        validateCrewMember(crewId, userId);
        List<Meeting> meetings = meetingReader.getNotEndedMeetingsByCrewId(crewId);
        return convertMeetingsToMeetingDtos(meetings, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MeetingDto> getPastMeetingDtosByCrewId(Long crewId, Long userId) {
        validateCrewMember(crewId, userId);
        List<Meeting> meetings = meetingReader.getEndedMeetingsByCrewId(crewId);
        return convertMeetingsToMeetingDtos(meetings, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getUserImagesByMeeting(MeetingDto meetingDto) {
        UserDto meetingCreator = meetingDto.userDto();
        List<MeetingMember> otherMembers = meetingMemberReader.getOtherMeetingMembersByMeetingIdMaximum(meetingDto.id(), meetingDto.userDto().id(), NumberConstants.MAXIMUM_OF_MEETING_THUMBNAILS - 1);

        List<String> userImages = new LinkedList<>();
        userImages.add(meetingCreator.profileImage());
        otherMembers.forEach(meetingMember -> {
            User user = userReader.getUserById(meetingMember.getMeetingMemberPK().getUserId());
            userImages.add(imageReader.getUserImageLink(user.getProfileImage()));
        });
        return userImages;
    }

    @Override
    @Transactional
    public void updateMeeting(MeetingDto meetingDto, Long userId) {
        validateMeetingCreator(meetingDto.userDto().id(), userId);
        validateTimes(meetingDto.startTime(), meetingDto.endTime());
        if (meetingDto.capacity() < meetingDto.memberCount())
            throw new BusinessException(MeetingErrorCode.CAPACITY_FORBIDDEN);
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
        MeetingMemberDto meetingMemberDto = MeetingMemberDto.of(MeetingMemberPK.of(userId, meetingDto.id()));
        meetingMemberStore.addMember(meetingMemberDto);
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
        meetingMemberStore.removeMember(meetingMemberDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUserDtosByMeeting(MeetingDto meetingDto) {
        List<MeetingMember> meetingMembers = meetingMemberReader.getMeetingMembersByMeetingId(meetingDto.id());
        return convertMeetingMembersToUserDtos(meetingMembers);
    }

    private void validateMeetingCreator(Long meetingCreatorId, Long userId) {
        if (!Objects.equals(meetingCreatorId, userId))
            throw new BusinessException(MeetingErrorCode.FORBIDDEN);
    }

    private void validateCrewMember(Long crewId, Long userId) {
        if (!crewMemberReader.existByCrewIdAndUserId(crewId, userId))
            throw new BusinessException(CrewErrorCode.NOT_CREW_MEMBER);
    }

    private void validateTimes(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime.isAfter(endTime))
            throw new BusinessException(MeetingErrorCode.TURNED_OVER_TIMES);
        if (!Objects.equals(startTime.getDayOfYear(), endTime.getDayOfYear()))
            throw new BusinessException(MeetingErrorCode.NOT_SAME_DAY);
    }

    private List<MeetingDto> convertMeetingsToMeetingDtos(List<Meeting> meetings, Long userId) {
        return meetings.stream().map(meeting -> {
            boolean isParticipated = meetingMemberReader.existsByMeetingIdAndUserId(meeting.getId(), userId);
            String userImage = imageReader.getUserImageLink(meeting.getUser().getProfileImage());
            List<String> gymImages = imageReader.getImageLinks(GYM, meeting.getGym().getImages());
            return MeetingDto.from(meeting, isParticipated, userImage, gymImages);
        }).toList();
    }

    private List<UserDto> convertMeetingMembersToUserDtos(List<MeetingMember> meetingMembers) {
        return meetingMembers.stream().map(meetingMember -> {
            User user = userReader.getUserById(meetingMember.getMeetingMemberPK().getUserId());
            String userImage = imageReader.getUserImageLink(user.getProfileImage());
            return UserDto.from(user, userImage);
        }).toList();
    }
}
