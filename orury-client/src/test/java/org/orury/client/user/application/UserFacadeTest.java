package org.orury.client.user.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.orury.client.comment.application.CommentService;
import org.orury.client.global.WithCursorResponse;
import org.orury.client.review.application.ReviewService;
import org.orury.client.user.interfaces.request.UserInfoRequest;
import org.orury.client.user.interfaces.response.MyCommentResponse;
import org.orury.client.user.interfaces.response.MyPostResponse;
import org.orury.client.user.interfaces.response.MyReviewResponse;
import org.orury.domain.comment.domain.dto.CommentDto;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.gym.domain.dto.GymDto;
import org.orury.domain.post.domain.PostService;
import org.orury.domain.post.domain.dto.PostDto;
import org.orury.domain.review.domain.dto.ReviewDto;
import org.orury.domain.user.domain.dto.UserDto;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@DisplayName("UserFacadeTest")
class UserFacadeTest {
    private UserService userService;
    private PostService postService;
    private ReviewService reviewService;
    private CommentService commentService;
    private UserFacade userFacade;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        postService = mock(PostService.class);
        reviewService = mock(ReviewService.class);
        commentService = mock(CommentService.class);

        userFacade = new UserFacade(userService, postService, reviewService, commentService);
    }

    @Test
    @DisplayName("readMypage(Long id) test: UserId를 받아서 UserDto를 반환한다. [성공]")
    void should_returnUserDto() {
        //given
        Long userId = 1L;
        UserDto userDto = createUserDto(userId);

        given(userService.getUserDtoById(anyLong())).willReturn(userDto);

        //when
        UserDto actualUserDto = userFacade.readMypage(userId);

        //then
        assertThat(actualUserDto).isEqualTo(userDto);
        then(userService).should(times(1)).getUserDtoById(anyLong());
    }

    @Test
    @DisplayName("updateProfileImage(Long id, MultipartFile image) test: id, image를 받아 프로필사진을 업데이트 한다. [성공]")
    void should_updateProfileImage() {
        //given
        Long userId = 1L;
        MultipartFile image = mock(MultipartFile.class);
        UserDto userDto = createUserDto(userId);
        given(userService.getUserDtoById(anyLong())).willReturn(userDto);

        //when
        userFacade.updateProfileImage(userId, image);

        //then
        then(userService).should(times(1)).getUserDtoById(anyLong());
        then(userService).should(times(1)).updateProfileImage(any(), any());
    }

    @Test
    @DisplayName("updateUserInfo(Long id, UserInfoRequest userInfoRequest) test: id, request를 받아 유저 정보를 업데이트한다. ")
    void should_updateUserInfo() {
        //given
        Long userId = 1L;
        UserInfoRequest userInfoRequest = createUserInfoRequest();
        UserDto userDto = createUserDto(userId);
        given(userService.getUserDtoById(anyLong())).willReturn(userDto);

        //when
        userFacade.updateUserInfo(userId, userInfoRequest);

        //then
        then(userService).should(times(1)).getUserDtoById(anyLong());
        then(userService).should(times(1)).updateUserInfo(any());
    }

    @Test
    @DisplayName("getPostsByUserId(Long id, Long cursor) test: id, cursor 값을 입력받아 내가 쓴 게시글을 반환한다. [성공] ")
    void should_returnWithCursorPostsResponse() {
        //given
        Long userId = 1L;
        Long cursor = 0L;

        List<PostDto> postDtos = new ArrayList<>();
        for (int i = 10; i >= 1; i--) {
            postDtos.add(createPostDto((long) i));
        }

        WithCursorResponse<MyPostResponse> response = WithCursorResponse.of(postDtos.stream()
                .map(MyPostResponse::of)
                .toList(), cursor);

        given(postService.getPostDtosByUserId(anyLong(), anyLong(), any())).willReturn(postDtos);

        //when
        WithCursorResponse<MyPostResponse> actualResponse = userFacade.getPostsByUserId(userId, cursor);

        //then
        assertThat(actualResponse).isEqualTo(response);
        then(postService).should(times(1)).getPostDtosByUserId(anyLong(), anyLong(), any());
    }

    @Test
    @DisplayName("getReviewsByUserId(Long id, Long cursor) test: id, cursor 값을 입력받아 내가 쓴 리뷰를 반환한다. [성공] ")
    void should_returnWithCursorReviewsResponse() {
        //given
        Long userId = 1L;
        Long cursor = 0L;

        List<ReviewDto> reviewDtos = new ArrayList<>();
        for (int i = 10; i >= 1; i--) {
            reviewDtos.add(createReviewDto((long) i));
        }

        WithCursorResponse<MyReviewResponse> response = WithCursorResponse.of(reviewDtos.stream()
                .map(MyReviewResponse::of)
                .toList(), cursor);

        given(reviewService.getReviewDtosByUserId(anyLong(), anyLong(), any())).willReturn(reviewDtos);

        //when
        WithCursorResponse<MyReviewResponse> actualResponse = userFacade.getReviewsByUserId(userId, cursor);

        //then
        assertThat(actualResponse).isEqualTo(response);
        then(reviewService).should(times(1)).getReviewDtosByUserId(anyLong(), anyLong(), any());
    }

    @Test
    @DisplayName("getCommentsByUserId(Long id, Long cursor) test: id, cursor 값을 입력받아 내가 쓴 댓글을 반환한다. [성공] ")
    void should_returnWithCommentsCursorResponse() {
        //given
        Long userId = 1L;
        Long cursor = 0L;

        List<CommentDto> commentDtos = new ArrayList<>();
        for (int i = 10; i >= 1; i--) {
            commentDtos.add(createCommentDto((long) i));
        }

        WithCursorResponse<MyCommentResponse> response = WithCursorResponse.of(commentDtos.stream()
                .map(MyCommentResponse::of)
                .toList(), cursor);

        given(commentService.getCommentDtosByUserId(anyLong(), anyLong())).willReturn(commentDtos);

        //when
        WithCursorResponse<MyCommentResponse> actualResponse = userFacade.getCommentsByUserId(userId, cursor);

        //then
        assertThat(actualResponse).isEqualTo(response);
        then(commentService).should(times(1)).getCommentDtosByUserId(anyLong(), anyLong());
    }

    @Test
    @DisplayName("deleteUser(Long id) test: userId를 받아 해당하는 User를 삭제한다. [성공] ")
    void should_deleteUser() {
        //given
        Long userId = 1L;
        UserDto userDto = createUserDto(userId);

        given(userService.getUserDtoById(anyLong())).willReturn(userDto);

        //when
        userFacade.deleteUser(userId);

        //then
        then(userService).should(times(1)).getUserDtoById(anyLong());
    }

    private UserDto createUserDto(Long id) {
        return UserDto.of(
                id,
                "userEmail",
                "userNickname",
                "userPassword",
                1,
                1,
                LocalDate.now(),
                "test.png",
                LocalDateTime.of(1999, 3, 1, 7, 50),
                LocalDateTime.of(1999, 3, 1, 7, 50),
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
                createUserDto(id),
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
                37.513709,
                127.062144,
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
                createUserDto(1L),
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
                createUserDto(1L),
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
}