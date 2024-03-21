package org.orury.client.user.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.orury.client.config.FacadeTest;
import org.orury.client.global.WithCursorResponse;
import org.orury.client.user.interfaces.request.UserInfoRequest;
import org.orury.client.user.interfaces.response.MyCommentResponse;
import org.orury.client.user.interfaces.response.MyPostResponse;
import org.orury.client.user.interfaces.response.MyReviewResponse;
import org.orury.domain.comment.domain.dto.CommentDto;
import org.orury.domain.post.domain.dto.PostDto;
import org.orury.domain.review.domain.dto.ReviewDto;
import org.orury.domain.user.domain.dto.UserDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.orury.client.ClientFixtureFactory.TestUserInfoRequest.createUserInfoRequest;
import static org.orury.domain.CommentDomainFixture.TestCommentDto.createCommentDto;
import static org.orury.domain.PostDomainFixture.TestPostDto.createPostDto;
import static org.orury.domain.ReviewDomainFixture.TestReviewDto.createReviewDto;
import static org.orury.domain.UserDomainFixture.TestUserDto.createUserDto;

@DisplayName("UserFacadeTest")
class UserFacadeTest extends FacadeTest {

    @Test
    @DisplayName("readMypage(Long id) test: UserId를 받아서 UserDto를 반환한다. [성공]")
    void should_returnUserDto() {
        //given
        Long userId = 1L;
        UserDto userDto = createUserDto(userId).build().get();

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
        UserDto userDto = createUserDto(userId).build().get();
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
        UserInfoRequest userInfoRequest = createUserInfoRequest().build().get();
        UserDto userDto = createUserDto(userId).build().get();
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
            postDtos.add(createPostDto((long) i).build().get());
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
            reviewDtos.add(createReviewDto((long) i).build().get());
        }
        int myReaction = 1;

        WithCursorResponse<MyReviewResponse> response = WithCursorResponse.of(reviewDtos.stream()
                .map(reviewDto -> MyReviewResponse.of(reviewDto, myReaction))
                .toList(), cursor);

        given(reviewService.getReviewDtosByUserId(anyLong(), anyLong(), any())).willReturn(reviewDtos);
        given(reviewService.getReactionType(anyLong(), anyLong())).willReturn(myReaction);

        //when
        WithCursorResponse<MyReviewResponse> actualResponse = userFacade.getReviewsByUserId(userId, cursor);

        //then
        assertThat(actualResponse).isEqualTo(response);
        then(reviewService).should(times(1)).getReviewDtosByUserId(anyLong(), anyLong(), any());
        then(reviewService).should(times(reviewDtos.size())).getReactionType(anyLong(), anyLong());
    }

    @Test
    @DisplayName("getCommentsByUserId(Long id, Long cursor) test: id, cursor 값을 입력받아 내가 쓴 댓글을 반환한다. [성공] ")
    void should_returnWithCommentsCursorResponse() {
        //given
        Long userId = 1L;
        Long cursor = 0L;

        List<CommentDto> commentDtos = new ArrayList<>();
        for (int i = 10; i >= 1; i--) {
            commentDtos.add(createCommentDto((long) i).build().get());
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
        UserDto userDto = createUserDto(userId).build().get();

        given(userService.getUserDtoById(anyLong())).willReturn(userDto);

        //when
        userFacade.deleteUser(userId);

        //then
        then(userService).should(times(1)).getUserDtoById(anyLong());
    }
}