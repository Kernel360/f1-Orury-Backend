package org.fastcampus.oruryclient.user.db.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.fastcampus.oruryclient.config.ControllerTest;
import org.fastcampus.oruryclient.config.WithUserPrincipal;
import org.fastcampus.oruryclient.global.WithCursorResponse;
import org.fastcampus.oruryclient.global.constants.NumberConstants;
import org.fastcampus.oruryclient.user.converter.message.UserMessage;
import org.fastcampus.oruryclient.user.converter.response.MyCommentResponse;
import org.fastcampus.oruryclient.user.converter.response.MyPostResponse;
import org.fastcampus.oruryclient.user.converter.response.MyReviewResponse;
import org.fastcampus.orurydomain.comment.dto.CommentDto;
import org.fastcampus.orurydomain.gym.dto.GymDto;
import org.fastcampus.orurydomain.post.dto.PostDto;
import org.fastcampus.orurydomain.review.dto.ReviewDto;
import org.fastcampus.orurydomain.user.dto.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
                .andExpect(jsonPath("$.data.list[0].id").value(response.list().get(0).id()))
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
                .andExpect(jsonPath("$.data.list[0].id").value(response.list().get(0).id()))
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
                .andExpect(jsonPath("$.data.list[0].id").value(response.list().get(0).id()))
        ;

        then(commentService).should()
                .getCommentDtosByUserId(
                        NumberConstants.USER_ID,
                        cursor,
                        pageable
                );
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
                LocalDateTime.now()
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
                "",
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
                "image.png",
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
                "test.png, test2.png",
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
}
