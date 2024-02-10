package org.fastcampus.oruryclient.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.fastcampus.oruryclient.config.ControllerTest;
import org.fastcampus.oruryclient.config.WithUserPrincipal;
import org.fastcampus.oruryclient.global.WithCursorResponse;
import org.fastcampus.oruryclient.user.converter.message.UserMessage;
import org.fastcampus.oruryclient.user.converter.request.UserInfoRequest;
import org.fastcampus.oruryclient.user.converter.response.MyCommentResponse;
import org.fastcampus.oruryclient.user.converter.response.MyPostResponse;
import org.fastcampus.oruryclient.user.converter.response.MyReviewResponse;
import org.fastcampus.oruryclient.user.converter.response.MypageResponse;
import org.fastcampus.orurydomain.comment.dto.CommentDto;
import org.fastcampus.orurydomain.global.constants.NumberConstants;
import org.fastcampus.orurydomain.gym.dto.GymDto;
import org.fastcampus.orurydomain.post.dto.PostDto;
import org.fastcampus.orurydomain.review.dto.ReviewDto;
import org.fastcampus.orurydomain.user.dto.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@Disabled
@DisplayName("[Controller] 유저 마이페이지 관련 테스트")
@WithUserPrincipal
public class UserControllerTest extends ControllerTest {

    @DisplayName("[GET] userId와 cursor값 0으로 내가 쓴 게시글을 처음 조회한다. - 성공")
    @Test
    void given_UserIdAndCursor_When_GetMyPosts_Then_Successfully() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, NumberConstants.POST_PAGINATION_SIZE);
        UserMessage code = UserMessage.USER_POSTS_READ;
        Long cursor = 0L;

        List<PostDto> postDtos = new ArrayList<>();
        for (int i = 10; i >= 1; i--) {
            postDtos.add(createPostDto((long) i));
        }

        WithCursorResponse<MyPostResponse> response = WithCursorResponse.of(postDtos.stream()
                .map(MyPostResponse::of)
                .toList(), cursor);

        given(postService.getPostDtosByUserId(
                NumberConstants.USER_ID,
                cursor,
                pageable)
        ).willReturn(postDtos);

        //when & then
        mvc.perform(get("/api/v1/user/posts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("cursor", "" + cursor)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(code.getMessage()))
                .andExpect(jsonPath("$.data.cursor").value(1))
                .andExpect(jsonPath("$.data.list[0].id").value(response.list()
                        .get(0)
                        .id()))
        ;

        then(postService).should()
                .getPostDtosByUserId(
                        NumberConstants.USER_ID,
                        cursor,
                        pageable
                );
    }

    @DisplayName("[GET] userId와 cursor값 0으로 내가 쓴 리뷰를 처음 조회한다. - 성공")
    @Test
    void given_UserIdAndCursor_When_GetMyReviews_Then_Successfully() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, NumberConstants.REVIEW_PAGINATION_SIZE);
        UserMessage code = UserMessage.USER_REVIEWS_READ;
        Long cursor = 0L;

        List<ReviewDto> reviewDtos = new ArrayList<>();
        for (int i = 10; i >= 1; i--) {
            reviewDtos.add(createReviewDto((long) i));
        }

        WithCursorResponse<MyReviewResponse> response = WithCursorResponse.of(reviewDtos.stream()
                .map(MyReviewResponse::of)
                .toList(), cursor);

        given(reviewService.getReviewDtosByUserId(
                NumberConstants.USER_ID,
                cursor,
                pageable)
        ).willReturn(reviewDtos);

        //when & then
        mvc.perform(get("/api/v1/user/reviews")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("cursor", "" + cursor)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(code.getMessage()))
                .andExpect(jsonPath("$.data.cursor").value(1))
                .andExpect(jsonPath("$.data.list[0].id").value(response.list()
                        .get(0)
                        .id()))
        ;

        then(reviewService).should()
                .getReviewDtosByUserId(
                        NumberConstants.USER_ID,
                        cursor,
                        pageable
                );
    }

    @DisplayName("[GET] userId와 cursor값 0으로 내가 쓴 댓글을 처음 조회한다. - 성공")
    @Test
    void given_UserIdAndCursor_When_GetMyCommments_Then_Successfully() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, NumberConstants.COMMENT_PAGINATION_SIZE);
        UserMessage code = UserMessage.USER_COMMENTS_READ;
        Long cursor = 0L;

        List<CommentDto> commentDtos = new ArrayList<>();
        for (int i = 10; i >= 1; i--) {
            commentDtos.add(createCommentDto((long) i));
        }

        WithCursorResponse<MyCommentResponse> response = WithCursorResponse.of(commentDtos.stream()
                .map(MyCommentResponse::of)
                .toList(), cursor);

        given(commentService.getCommentDtosByUserId(
                NumberConstants.USER_ID,
                cursor,
                pageable)
        ).willReturn(commentDtos);

        //when & then
        mvc.perform(get("/api/v1/user/comments")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("cursor", "" + cursor)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(code.getMessage()))
                .andExpect(jsonPath("$.data.cursor").value(1))
                .andExpect(jsonPath("$.data.list[0].id").value(response.list()
                        .get(0)
                        .id()))
        ;

        then(commentService).should()
                .getCommentDtosByUserId(
                        NumberConstants.USER_ID,
                        cursor,
                        pageable
                );
    }

    @DisplayName("[GET] - 로그인한 유저의 마이페이지 화면을 조회한다.")
    @Test
    void when_RetrieveUsersMypage_Then_Successfully() throws Exception {
        // given
        UserDto userDto = createUserDto();
        given(userService.getUserDtoById(
                NumberConstants.USER_ID)
        ).willReturn(userDto);
        MypageResponse mypageResponse = MypageResponse.of(userDto);

        //when & then
        mvc.perform(get("/api/v1/user")
                        .with(csrf())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(UserMessage.USER_READ.getMessage()))
                .andExpect(jsonPath("$.data.id").value(mypageResponse.id()))
                .andExpect(jsonPath("$.data.email").value(mypageResponse.email()))
                .andExpect(jsonPath("$.data.nickname").value(mypageResponse.nickname()))
                .andExpect(jsonPath("$.data.sign_up_type").value(mypageResponse.signUpType()))
                .andExpect(jsonPath("$.data.gender").value(mypageResponse.gender()))
                .andExpect(jsonPath("$.data.birthday").value(mypageResponse.birthday()))
                .andExpect(jsonPath("$.data.profile_image").value(mypageResponse.profileImage()));

        then(userService).should()
                .getUserDtoById(
                        NumberConstants.USER_ID
                );
    }

    @DisplayName("[POST] - 로그인한 유저의 프로필 사진 수정 시 정상적으로 수정된다.")
    @Test
    void when_EditUsersProfileImage_Then_Successfully() throws Exception {
        // given
        UserDto userDto = createUserDto();
        MockMultipartFile image = createImagePart();

        given(userService.getUserDtoById(NumberConstants.USER_ID)).willReturn(userDto);

        // when
        mvc.perform(multipart(POST, "/api/v1/user/profile-image")
                        .file(image)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(UserMessage.USER_PROFILEIMAGE_UPDATED.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty())
        ;

        // then
        then(userService).should(times(1)).updateProfileImage(any(), any());
    }

    @DisplayName("[PATCH] - request에 담긴 유저 정보 수정 시 성공적으로 수정된다.")
    @Test
    void given_UserInfoRequest_When_EditUserInfo_Then_Successfully() throws Exception {
        // given
        UserDto userDto = createUserDto();
        given(userService.getUserDtoById(NumberConstants.USER_ID)).willReturn(userDto);

        UserInfoRequest userInfoRequest = createUserInfoRequest();
        UserDto updateUserDto = UserInfoRequest.toDto(userDto, userInfoRequest);

        // when
        mvc.perform(patch("/api/v1/user")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userInfoRequest))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(UserMessage.USER_UPDATED.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty())
        ;

        // then

        then(userService).should(times(1)).updateUserInfo(any());
    }

    @DisplayName("[DELETE] - 로그인한 유저의 계정을 탈퇴 처리한다.")
    @Test
    void when_DeleteUser_then_Successfully() throws Exception {
        // given
        UserDto userDto = createUserDto();
        given(userService.getUserDtoById(NumberConstants.USER_ID)).willReturn(userDto);

        //when & then
        mvc.perform(delete("/api/v1/user")
                        .with(csrf())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(UserMessage.USER_DELETED.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty())
        ;

        then(userService).should(times(1)).deleteUser(userDto);
    }

    private UserDto createUserDto() {
        return UserDto.of(
                NumberConstants.USER_ID,
                "mail@mail",
                "user",
                "{noop}password",
                1,
                1,
                null,
                "bio",
                LocalDateTime.now(),
                LocalDateTime.now(),
                NumberConstants.IS_NOT_DELETED
        );
    }

    private PostDto createPostDto(Long id) {
        return PostDto.of(
                id,
                "title",
                "content",
                0,
                0,
                0,
                List.of(),
                1,
                createUserDto(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    private GymDto createGymDto() {
        return GymDto.of(
                1L,
                "더클라임 봉은사점",
                "kakaoid",
                "서울시 도로명주소",
                "서울시 지번주소",
                25.3f,
                23,
                12,
                List.of(),
                "37.513709",
                "127.062144",
                "더클라임",
                "01012345678",
                "instalink.com",
                "MONDAY",
                LocalDateTime.now(),
                LocalDateTime.now(),
                "11:00-23:11",
                "12:00-23:22",
                "13:00-23:33",
                "14:00-23:44",
                "15:00-23:55",
                "16:00-23:66",
                "17:00-23:77",
                "gymHomepageLink",
                "gymRemark"
        );
    }

    private ReviewDto createReviewDto(Long id) {
        return ReviewDto.of(
                id,
                "test content",
                List.of(),
                4.5f,
                1,
                2,
                3,
                4,
                5,
                createUserDto(),
                createGymDto(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    private CommentDto createCommentDto(Long id) {
        return CommentDto.of(
                id,
                "content",
                1L,
                0,
                createPostDto(id),
                createUserDto(),
                0,
                null,
                null
        );
    }

    private UserInfoRequest createUserInfoRequest() {
        return new UserInfoRequest(
                "nickname"
        );
    }

    private MockMultipartFile createImagePart() {
        return new MockMultipartFile(
                "TestImageName",
                "testImageFileName",
                MediaType.TEXT_PLAIN_VALUE,
                "testImageFileDate".getBytes()
        );
    }

    private MockMultipartFile createRequestPart(Object request) throws JsonProcessingException {
        return new MockMultipartFile(
                "request",
                "testRequest",
                MediaType.APPLICATION_JSON_VALUE,
                mapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8)
        );
    }
}
