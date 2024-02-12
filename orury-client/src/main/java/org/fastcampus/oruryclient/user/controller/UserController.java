package org.fastcampus.oruryclient.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryclient.comment.service.CommentService;
import org.fastcampus.oruryclient.global.WithCursorResponse;
import org.fastcampus.oruryclient.post.service.PostService;
import org.fastcampus.oruryclient.review.service.ReviewService;
import org.fastcampus.oruryclient.user.converter.message.UserMessage;
import org.fastcampus.oruryclient.user.converter.request.UserInfoRequest;
import org.fastcampus.oruryclient.user.converter.response.MyCommentResponse;
import org.fastcampus.oruryclient.user.converter.response.MyPostResponse;
import org.fastcampus.oruryclient.user.converter.response.MyReviewResponse;
import org.fastcampus.oruryclient.user.converter.response.MypageResponse;
import org.fastcampus.oruryclient.user.service.UserService;
import org.fastcampus.orurydomain.base.converter.ApiResponse;
import org.fastcampus.orurydomain.comment.dto.CommentDto;
import org.fastcampus.orurydomain.global.constants.NumberConstants;
import org.fastcampus.orurydomain.post.dto.PostDto;
import org.fastcampus.orurydomain.review.dto.ReviewDto;
import org.fastcampus.orurydomain.user.dto.UserDto;
import org.fastcampus.orurydomain.user.dto.UserPrincipal;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
//
@RequestMapping("/api/v1/user")
@RestController
public class UserController {

    //
    private final UserService userService;
    private final PostService postService;
    private final ReviewService reviewService;
    private final CommentService commentService;

    @Operation(summary = "마이페이지 조회", description = "id에 해당하는 유저의 정보를 조회합니다. 닉네임, 생일, 프로필사진, 이메일, 성별이 return 됩니다. ")
    @GetMapping()
    public ApiResponse<Object> readMypage(@AuthenticationPrincipal UserPrincipal userPrincipal) {

        UserDto userDto = userService.getUserDtoById(userPrincipal.id());
        MypageResponse mypageResponse = MypageResponse.of(userDto);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(UserMessage.USER_READ.getMessage())
                .data(mypageResponse)
                .build();
    }

    @Operation(summary = "프로필 사진 수정", description = "request에 담긴 id에 해당하는 유저의 프로필 사진을 수정합니다.")
    @PostMapping("/profile-image")
    public ApiResponse<Object> updateProfileImage(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestPart(required = false) MultipartFile image
    ) {
        UserDto userDto = userService.getUserDtoById(userPrincipal.id());
        userService.updateProfileImage(userDto, image);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(UserMessage.USER_PROFILEIMAGE_UPDATED.getMessage())
                .build();
    }

    @Operation(summary = "유저 정보 수정", description = "request에 담긴 id에 해당하는 유저의 정보를 수정합니다. 현재 닉네임만 수정 가능합니다. ")
    @PatchMapping
    public ApiResponse<Object> updateUserInfo(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody UserInfoRequest userInfoRequest
    ) {
        UserDto userDto = userService.getUserDtoById(userPrincipal.id());
        UserDto updateUserDto = UserInfoRequest.toDto(userDto, userInfoRequest);

        userService.updateUserInfo(updateUserDto);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(UserMessage.USER_UPDATED.getMessage())
                .build();

    }

    @Operation(summary = "내가 쓴 게시글 조회", description = "user_id로 내가 쓴 게시글을 조회한다.")
    @GetMapping("/posts")
    public ApiResponse<WithCursorResponse<MyPostResponse>> getPostsByUserId(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam Long cursor) {

        List<PostDto> postDtos = postService.getPostDtosByUserId(userPrincipal.id(), cursor, PageRequest.of(0, NumberConstants.POST_PAGINATION_SIZE));
        List<MyPostResponse> myPostResponses = postDtos.stream()
                .map(MyPostResponse::of)
                .toList();

        WithCursorResponse<MyPostResponse> cursorResponse = WithCursorResponse.of(myPostResponses, cursor);

        return ApiResponse.<WithCursorResponse<MyPostResponse>>builder()
                .status(HttpStatus.OK.value())
                .message(UserMessage.USER_POSTS_READ.getMessage())
                .data(cursorResponse)
                .build();
    }

    @Operation(summary = "내가 쓴 리뷰 조회", description = "user_id로 내가 쓴 리뷰를 조회한다.")
    @GetMapping("/reviews")
    public ApiResponse<WithCursorResponse<MyReviewResponse>> getReviewsByUserId(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam Long cursor) {

        List<ReviewDto> reviewDtos = reviewService.getReviewDtosByUserId(userPrincipal.id(), cursor, PageRequest.of(0, NumberConstants.POST_PAGINATION_SIZE));
        List<MyReviewResponse> myReviewResponses = reviewDtos.stream()
                .map(MyReviewResponse::of)
                .toList();

        WithCursorResponse<MyReviewResponse> cursorResponse = WithCursorResponse.of(myReviewResponses, cursor);

        return ApiResponse.<WithCursorResponse<MyReviewResponse>>builder()
                .status(HttpStatus.OK.value())
                .message(UserMessage.USER_REVIEWS_READ.getMessage())
                .data(cursorResponse)
                .build();
    }

    @Operation(summary = "내가 쓴 댓글 조회", description = "user_id로 내가 쓴 댓글을 조회한다.")
    @GetMapping("/comments")
    public ApiResponse<WithCursorResponse<MyCommentResponse>> getCommentsByUserId(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam Long cursor) {

        List<CommentDto> commmentDtos = commentService.getCommentDtosByUserId(userPrincipal.id(), cursor, PageRequest.of(0, NumberConstants.POST_PAGINATION_SIZE));
        List<MyCommentResponse> myCommentResponses = commmentDtos.stream()
                .map(MyCommentResponse::of)
                .toList();

        WithCursorResponse<MyCommentResponse> cursorResponse = WithCursorResponse.of(myCommentResponses, cursor);

        return ApiResponse.<WithCursorResponse<MyCommentResponse>>builder()
                .status(HttpStatus.OK.value())
                .message(UserMessage.USER_COMMENTS_READ.getMessage())
                .data(cursorResponse)
                .build();
    }

    @Operation(summary = "회원 탈퇴", description = "id에 해당하는 회원을 탈퇴합니다. ")
    @DeleteMapping
    public ApiResponse<Object> deleteUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        UserDto user = userService.getUserDtoById(userPrincipal.id());
        userService.deleteUser(user);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(UserMessage.USER_DELETED.getMessage())
                .build();
    }

}
