package org.orury.client.comment.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.orury.client.comment.interfaces.request.CommentCreateRequest;
import org.orury.client.comment.interfaces.request.CommentUpdateRequest;
import org.orury.client.comment.interfaces.response.CommentsWithCursorResponse;
import org.orury.client.config.FacadeTest;
import org.orury.domain.comment.domain.dto.CommentDto;
import org.orury.domain.comment.domain.dto.CommentLikeDto;
import org.orury.domain.comment.domain.entity.CommentLikePK;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.post.domain.dto.PostDto;
import org.orury.domain.user.domain.dto.UserDto;
import org.orury.domain.user.domain.dto.UserStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@DisplayName("[Facade] 댓글 Facade 테스트")
class CommentFacadeTest extends FacadeTest {

    @DisplayName("댓글생성Request와 유저id를 받으면, 댓글을 생성한다.")
    @Test
    void should_CreateComment() {
        // given
        CommentCreateRequest commentCreateRequest = createCommentCreateRequest();
        Long userId = 1L;

        // when
        commentFacade.createComment(commentCreateRequest, userId);

        // then
        then(userService).should(times(1))
                .getUserDtoById(anyLong());
        then(postService).should(times(1))
                .getPostDtoById(anyLong());
        then(commentService).should(times(1))
                .createComment(any());
    }

    @DisplayName("게시글id/cursor/유저id를 받으면, CommentsWithCursorReponse를 반환한다")
    @Test
    void should_RetrieveCommentsResponseWithCursor() {
        // given
        Long postId = 2L;
        Long cursor = 33L;
        Long userId = 1L;
        PostDto postDto = createPostDto();
        List<CommentDto> commentDtos = List.of(
                createCommentDto(24L),
                createDeletedCommentDto(29L),
                createCommentDto(31L)
        );

        given(postService.getPostDtoById(postId))
                .willReturn(postDto);
        given(commentService.getCommentDtosByPost(postDto, cursor))
                .willReturn(commentDtos);
        given(commentService.isLiked(anyLong(), anyLong()))
                .willReturn(true, false, false);

        // when
        commentFacade.getCommentsByPostId(postId, cursor, userId);

        // then
        then(postService).should(times(1))
                .getPostDtoById(anyLong());
        then(commentService).should(times(1))
                .getCommentDtosByPost(any(), anyLong());
        then(commentService).should(times(commentDtos.size()))
                .isLiked(anyLong(), anyLong());
    }

    @DisplayName("게시글id와 cursor에 따른 댓글목록이 조회되지 않으면, CommentsWithCursorReponse의 cursor에 -1(조회된 값이 없음)를 담아 반환한다")
    @Test
    void when_EmptyComments_Then_RetrieveCommentsResponseWithCursorWithMinusTwoCursor() {
        // given
        Long postId = 2L;
        Long cursor = 33L;
        Long userId = 1L;
        PostDto postDto = createPostDto();

        given(postService.getPostDtoById(postId))
                .willReturn(postDto);
        given(commentService.getCommentDtosByPost(postDto, cursor))
                .willReturn(Collections.emptyList());

        // when
        CommentsWithCursorResponse response = commentFacade.getCommentsByPostId(postId, cursor, userId);

        // then
        assertEquals(NumberConstants.LAST_CURSOR, response.cursor());
        assertEquals(0, response.comments().size());

        then(postService).should(times(1))
                .getPostDtoById(anyLong());
        then(commentService).should(times(1))
                .getCommentDtosByPost(any(), anyLong());
        then(commentService).should(never())
                .isLiked(anyLong(), anyLong());
    }

    @DisplayName("댓글생성Request와 유저id를 받으면, 댓글을 수정한다.")
    @Test
    void should_UpdateComment() {
        // given
        Long commentId = 2L;
        CommentDto commentDto = createCommentDto(commentId);
        CommentUpdateRequest commentUpdateRequest = createCommentUpdateRequest(commentId);
        Long userId = 3L;

        given(commentService.getCommentDtoById(commentId))
                .willReturn(commentDto);

        // when
        commentFacade.updateComment(commentUpdateRequest, userId);

        // then
        then(commentService).should(times(1))
                .getCommentDtoById(anyLong());
        then(commentService).should(times(1))
                .updateComment(any(), anyLong());

    }

    @DisplayName("댓글id와 유저id를 받으면, 댓글을 삭제한다.")
    @Test
    void should_DeleteComment() {
        // given
        Long commentId = 2L;
        CommentDto commentDto = createCommentDto(commentId);
        Long userId = 3L;

        given(commentService.getCommentDtoById(commentId))
                .willReturn(commentDto);

        // when
        commentFacade.deleteComment(commentId, userId);

        // then
        then(commentService).should(times(1))
                .getCommentDtoById(anyLong());
        then(commentService).should(times(1))
                .deleteComment(any(), anyLong());
    }

    @DisplayName("댓글좋아요Dto를 받으면, 댓글좋아요를 생성한다.")
    @Test
    void should_CreateGymLike() {
        // given
        Long userId = 3L;
        Long commentId = 4L;
        CommentLikeDto commentLikeDto = createCommentLikeDto(userId, commentId);

        // when
        commentFacade.createCommentLike(commentLikeDto);

        // then
        then(commentService).should(times(1))
                .createCommentLike(any());
    }

    @DisplayName("댓글좋아요Dto를 받으면, 댓글좋아요를 삭제한다.")
    @Test
    void should_DeleteGymLike() {
        // given
        Long userId = 3L;
        Long commentId = 4L;
        CommentLikeDto commentLikeDto = createCommentLikeDto(userId, commentId);

        // when
        commentFacade.deleteCommentLike(commentLikeDto);

        // then
        then(commentService).should(times(1))
                .deleteCommentLike(any());
    }

    private CommentCreateRequest createCommentCreateRequest() {
        return CommentCreateRequest.of(
                "content",
                null,
                11L
        );
    }

    private CommentUpdateRequest createCommentUpdateRequest(Long commentId) {
        return CommentUpdateRequest.of(
                commentId,
                "content"
        );
    }

    private UserDto createUserDto() {
        return UserDto.of(
                2L,
                "userEmail",
                "userNickname",
                "userPassword",
                1,
                1,
                LocalDate.now(),
                "userProfileImage",
                LocalDateTime.of(1999, 3, 1, 7, 50),
                LocalDateTime.of(1999, 3, 1, 7, 50),
                UserStatus.ENABLE
        );
    }

    private PostDto createPostDto() {
        return PostDto.of(
                1L,
                "postTitle",
                "postContent",
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

    private CommentDto createCommentDto(Long commentId) {
        return CommentDto.of(
                commentId,
                "commentContent",
                23L,
                0,
                createPostDto(),
                createUserDto(),
                NumberConstants.IS_NOT_DELETED,
                LocalDateTime.of(2024, 1, 1, 11, 50),
                LocalDateTime.of(2024, 1, 1, 11, 50)
        );
    }

    private CommentDto createDeletedCommentDto(Long commentId) {
        return CommentDto.of(
                commentId,
                "commentContent",
                NumberConstants.PARENT_COMMENT,
                0,
                createPostDto(),
                createUserDto(),
                NumberConstants.IS_DELETED,
                LocalDateTime.of(2024, 1, 1, 11, 50),
                LocalDateTime.of(2024, 1, 1, 11, 50)
        );
    }

    private CommentLikeDto createCommentLikeDto(Long userId, Long commentId) {
        return CommentLikeDto.of(CommentLikePK.of(userId, commentId));
    }
}
