package org.orury.client.user.interfaces;

import org.orury.client.global.WithCursorResponse;
import org.orury.client.user.application.UserFacade;
import org.orury.client.user.interfaces.message.UserMessage;
import org.orury.client.user.interfaces.request.UserInfoRequest;
import org.orury.client.user.interfaces.response.MyCommentResponse;
import org.orury.client.user.interfaces.response.MyPostResponse;
import org.orury.client.user.interfaces.response.MyReviewResponse;
import org.orury.client.user.interfaces.response.MypageResponse;
import org.orury.domain.base.converter.ApiResponse;
import org.orury.domain.user.domain.dto.UserDto;
import org.orury.domain.user.domain.dto.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {
    private final UserFacade userFacade;

    @Operation(summary = "마이페이지 조회", description = "id에 해당하는 유저의 정보를 조회합니다. 닉네임, 생일, 프로필사진, 이메일, 성별이 return 됩니다. ")
    @GetMapping()
    public ApiResponse readMypage(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        UserDto userDto = userFacade.readMypage(userPrincipal.id());
        MypageResponse mypageResponse = MypageResponse.of(userDto);

        return ApiResponse.of(UserMessage.USER_READ.getMessage(), mypageResponse);
    }

    @Operation(summary = "프로필 사진 수정", description = "request에 담긴 id에 해당하는 유저의 프로필 사진을 수정합니다.")
    @PostMapping("/profile-image")
    public ApiResponse updateProfileImage(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestPart(required = false) MultipartFile image
    ) {
        userFacade.updateProfileImage(userPrincipal.id(), image);
        return ApiResponse.of(UserMessage.USER_PROFILEIMAGE_UPDATED.getMessage());
    }

    @Operation(summary = "유저 정보 수정", description = "request에 담긴 id에 해당하는 유저의 정보를 수정합니다. 현재 닉네임만 수정 가능합니다. ")
    @PatchMapping
    public ApiResponse updateUserInfo(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody UserInfoRequest userInfoRequest
    ) {
        userFacade.updateUserInfo(userPrincipal.id(), userInfoRequest);

        return ApiResponse.of(UserMessage.USER_UPDATED.getMessage());
    }

    @Operation(summary = "내가 쓴 게시글 조회", description = "user_id로 내가 쓴 게시글을 조회한다.")
    @GetMapping("/posts")
    public ApiResponse getPostsByUserId(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam Long cursor) {
        WithCursorResponse<MyPostResponse> cursorResponse = userFacade.getPostsByUserId(userPrincipal.id(), cursor);

        return ApiResponse.of(UserMessage.USER_POSTS_READ.getMessage(), cursorResponse);
    }

    @Operation(summary = "내가 쓴 리뷰 조회", description = "user_id로 내가 쓴 리뷰를 조회한다.")
    @GetMapping("/reviews")
    public ApiResponse getReviewsByUserId(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam Long cursor) {
        WithCursorResponse<MyReviewResponse> cursorResponse = userFacade.getReviewsByUserId(userPrincipal.id(), cursor);

        return ApiResponse.of(UserMessage.USER_REVIEWS_READ.getMessage(), cursorResponse);
    }

    @Operation(summary = "내가 쓴 댓글 조회", description = "user_id로 내가 쓴 댓글을 조회한다.")
    @GetMapping("/comments")
    public ApiResponse getCommentsByUserId(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam Long cursor) {
        WithCursorResponse<MyCommentResponse> cursorResponse = userFacade.getCommentsByUserId(userPrincipal.id(), cursor);

        return ApiResponse.of(UserMessage.USER_COMMENTS_READ.getMessage(), cursorResponse);
    }

    @Operation(summary = "회원 탈퇴", description = "id에 해당하는 회원을 탈퇴합니다. ")
    @DeleteMapping
    public ApiResponse deleteUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        userFacade.deleteUser(userPrincipal.id());

        return ApiResponse.of(UserMessage.USER_DELETED.getMessage());
    }
}
