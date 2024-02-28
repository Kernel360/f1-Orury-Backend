package org.orury.client.user.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.orury.client.comment.service.CommentService;
import org.orury.client.global.WithCursorResponse;
import org.orury.client.post.service.PostService;
import org.orury.client.review.service.ReviewService;
import org.orury.client.user.interfaces.request.UserInfoRequest;
import org.orury.client.user.interfaces.response.MyPostResponse;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.post.domain.dto.PostDto;
import org.orury.domain.user.domain.UserService;
import org.orury.domain.user.domain.dto.UserDto;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @DisplayName("getPostsByUserId(Long id, Long cursor) test: id, cursor 값을 입력받아 WithCursorResponse을 반환한다. [성공] ")
    void should_returnWithCursorResponse() {
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

    // review, comment 받아서 추가 메서드 구현 예정 (컨트롤러도 리팩토링 한번 더 해야함)

    private static UserDto createUserDto(Long id) {
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

    private UserInfoRequest createUserInfoRequest() {
        return new UserInfoRequest(
                "nickname"
        );
    }
}