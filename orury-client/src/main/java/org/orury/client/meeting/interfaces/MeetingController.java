package org.orury.client.meeting.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.client.meeting.application.MeetingFacade;
import org.orury.client.meeting.interfaces.request.MeetingCreateRequest;
import org.orury.client.meeting.interfaces.request.MeetingUpdateRequest;
import org.orury.client.meeting.interfaces.response.MeetingMembersResponse;
import org.orury.client.meeting.interfaces.response.MeetingsResponse;
import org.orury.domain.base.converter.ApiResponse;
import org.orury.domain.user.domain.dto.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.orury.client.meeting.interfaces.message.MeetingMessage.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/crews")
@RestController
public class MeetingController {
    private final MeetingFacade meetingFacade;

    @Operation(summary = "일정 생성", description = "MeetingCreateRequest를 받아, 일정을 생성한다.")
    @PostMapping("/meetings")
    public ApiResponse createMeeting(@RequestBody MeetingCreateRequest request, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        meetingFacade.createMeeting(request, userPrincipal.id());
        return ApiResponse.of(MEETING_CREATED.getMessage());
    }

    @Operation(summary = "현재 일정 목록 조회", description = "crewId를 받아, 현재 일정 목록을 반환한다.")
    @GetMapping("/{crewId}/meetings/present")
    public ApiResponse getPresentMeetings(@PathVariable Long crewId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<MeetingsResponse> meetingsResponses = meetingFacade.getPresentMeetings(crewId, userPrincipal.id());
        return ApiResponse.of(MEETINGS_READ.getMessage(), meetingsResponses);
    }

    @Operation(summary = "과거 일정 목록 조회", description = "crewId를 받아, 과거 일정 목록을 반환한다.")
    @GetMapping("/{crewId}/meetings/past")
    public ApiResponse getPastMeetings(@PathVariable Long crewId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<MeetingsResponse> meetingsResponses = meetingFacade.getPastMeetings(crewId, userPrincipal.id());
        return ApiResponse.of(MEETINGS_READ.getMessage(), meetingsResponses);
    }

    @Operation(summary = "일정 수정", description = "MeetingUpdateRequest를 받아, 일정을 수정한다.")
    @PatchMapping("/meetings")
    public ApiResponse updateMeeting(@RequestBody MeetingUpdateRequest request, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        meetingFacade.updateMeeting(request, userPrincipal.id());
        return ApiResponse.of(MEETING_UPDATED.getMessage());
    }

    @Operation(summary = "일정 삭제", description = "meetingId를 받아, 일정을 삭제한다.")
    @DeleteMapping("/meetings/{meetingId}")
    public ApiResponse deleteMeeting(@PathVariable Long meetingId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        meetingFacade.deleteMeeting(meetingId, userPrincipal.id());
        return ApiResponse.of(MEETING_DELETED.getMessage());
    }

    @Operation(summary = "일정멤버 추가", description = "meetingId를 받아, 일정멤버를 추가한다.")
    @PostMapping("/meetings/{meetingId}/members")
    public ApiResponse addMeetingMember(@PathVariable Long meetingId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        meetingFacade.addMeetingMember(meetingId, userPrincipal.id());
        return ApiResponse.of(MEETING_MEMBER_ADDED.getMessage());
    }

    @Operation(summary = "일정멤버 제거", description = "meetingId를 받아, 일정멤버를 제거한다.")
    @DeleteMapping("/meetings/{meetingId}/members")
    public ApiResponse removeMeetingMember(@PathVariable Long meetingId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        meetingFacade.removeMeetingMember(meetingId, userPrincipal.id());
        return ApiResponse.of(MEETING_MEMBER_REMOVED.getMessage());
    }

    @Operation(summary = "일정멤버 목록 조회", description = "meetingId를 받아, 일정멤버 목록을 조회한다.")
    @GetMapping("/meetings/{meetingId}/members")
    public ApiResponse getMeetingMembers(@PathVariable Long meetingId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<MeetingMembersResponse> response = meetingFacade.getMeetingMembers(meetingId, userPrincipal.id());
        return ApiResponse.of(MEETING_MEMBERS_READ.getMessage(), response);
    }
}
