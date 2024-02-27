package org.orury.client.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.client.global.WithCursorResponse;
import org.orury.client.post.service.PostService;
import org.orury.client.review.service.ReviewService;
import org.orury.client.user.converter.message.UserMessage;
import org.orury.client.user.converter.request.UserInfoRequest;
import org.orury.client.user.converter.response.MyCommentResponse;
import org.orury.client.user.converter.response.MyPostResponse;
import org.orury.client.user.converter.response.MyReviewResponse;
import org.orury.client.user.converter.response.MypageResponse;
import org.orury.client.user.service.UserService;
import org.orury.domain.base.converter.ApiResponse;
import org.orury.domain.comment.domain.CommentService;
import org.orury.domain.comment.domain.dto.CommentDto;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.post.dto.PostDto;
import org.orury.domain.review.dto.ReviewDto;
import org.orury.domain.user.dto.UserDto;
import org.orury.domain.user.dto.UserPrincipal;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {
    private final UserService userService;
    private final PostService postService;
    private final ReviewService reviewService;
    private final CommentService commentService;

    @Operation(summary = "마이페이지 조회", description = "id에 해당하는 유저의 정보를 조회합니다. 닉네임, 생일, 프로필사진, 이메일, 성별이 return 됩니다. ")
    @GetMapping()
    public ApiResponse readMypage(@AuthenticationPrincipal UserPrincipal userPrincipal) {

        UserDto userDto = userService.getUserDtoById(userPrincipal.id());
        MypageResponse mypageResponse = MypageResponse.of(userDto);

        return ApiResponse.of(UserMessage.USER_READ.getMessage(), mypageResponse);
    }

    @Operation(summary = "프로필 사진 수정", description = "request에 담긴 id에 해당하는 유저의 프로필 사진을 수정합니다.")
    @PostMapping("/profile-image")
    public ApiResponse updateProfileImage(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestPart(required = false) MultipartFile image
    ) {
        UserDto userDto = userService.getUserDtoById(userPrincipal.id());
        userService.updateProfileImage(userDto, image);

        return ApiResponse.of(UserMessage.USER_PROFILEIMAGE_UPDATED.getMessage());
    }

    @Operation(summary = "유저 정보 수정", description = "request에 담긴 id에 해당하는 유저의 정보를 수정합니다. 현재 닉네임만 수정 가능합니다. ")
    @PatchMapping
    public ApiResponse updateUserInfo(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody UserInfoRequest userInfoRequest
    ) {
        UserDto userDto = userService.getUserDtoById(userPrincipal.id());
        UserDto updateUserDto = UserInfoRequest.toDto(userDto, userInfoRequest);

        userService.updateUserInfo(updateUserDto);

        return ApiResponse.of(UserMessage.USER_UPDATED.getMessage());
    }

    @Operation(summary = "내가 쓴 게시글 조회", description = "user_id로 내가 쓴 게시글을 조회한다.")
    @GetMapping("/posts")
    public ApiResponse getPostsByUserId(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam Long cursor) {

        List<PostDto> postDtos = postService.getPostDtosByUserId(userPrincipal.id(), cursor, PageRequest.of(0, NumberConstants.POST_PAGINATION_SIZE));
        List<MyPostResponse> myPostResponses = postDtos.stream()
                .map(MyPostResponse::of)
                .toList();

        WithCursorResponse<MyPostResponse> cursorResponse = WithCursorResponse.of(myPostResponses, cursor);

        return ApiResponse.of(UserMessage.USER_POSTS_READ.getMessage(), cursorResponse);
    }

    @Operation(summary = "내가 쓴 리뷰 조회", description = "user_id로 내가 쓴 리뷰를 조회한다.")
    @GetMapping("/reviews")
    public ApiResponse getReviewsByUserId(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam Long cursor) {

        List<ReviewDto> reviewDtos = reviewService.getReviewDtosByUserId(userPrincipal.id(), cursor, PageRequest.of(0, NumberConstants.POST_PAGINATION_SIZE));
        List<MyReviewResponse> myReviewResponses = reviewDtos.stream()
                .map(MyReviewResponse::of)
                .toList();

        WithCursorResponse<MyReviewResponse> cursorResponse = WithCursorResponse.of(myReviewResponses, cursor);

        return ApiResponse.of(UserMessage.USER_REVIEWS_READ.getMessage(), cursorResponse);
    }

    @Operation(summary = "내가 쓴 댓글 조회", description = "user_id로 내가 쓴 댓글을 조회한다.")
    @GetMapping("/comments")
    public ApiResponse getCommentsByUserId(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam Long cursor) {

        List<CommentDto> commmentDtos = commentService.getCommentDtosByUserId(userPrincipal.id(), cursor);
        List<MyCommentResponse> myCommentResponses = commmentDtos.stream()
                .map(MyCommentResponse::of)
                .toList();

        WithCursorResponse<MyCommentResponse> cursorResponse = WithCursorResponse.of(myCommentResponses, cursor);

        return ApiResponse.of(UserMessage.USER_COMMENTS_READ.getMessage(), cursorResponse);
    }

    @Operation(summary = "회원 탈퇴", description = "id에 해당하는 회원을 탈퇴합니다. ")
    @DeleteMapping
    public ApiResponse deleteUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        UserDto user = userService.getUserDtoById(userPrincipal.id());
        userService.deleteUser(user);

        return ApiResponse.of(UserMessage.USER_DELETED.getMessage());
    }
}
