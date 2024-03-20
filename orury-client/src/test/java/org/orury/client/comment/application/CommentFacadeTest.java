package org.orury.client.comment.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.orury.client.comment.interfaces.request.CommentCreateRequest;
import org.orury.client.comment.interfaces.request.CommentUpdateRequest;
import org.orury.client.comment.interfaces.response.CommentsWithCursorResponse;
import org.orury.client.config.FacadeTest;
import org.orury.domain.comment.domain.dto.CommentDto;
import org.orury.domain.comment.domain.dto.CommentLikeDto;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.post.domain.dto.PostDto;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.orury.client.ClientFixtureFactory.TestCommentCreateRequest.createCommentCreateRequest;
import static org.orury.client.ClientFixtureFactory.TestCommentUpdateRequest.createCommentUpdateRequest;
import static org.orury.domain.DomainFixtureFactory.TestCommentDto.createCommentDto;
import static org.orury.domain.DomainFixtureFactory.TestCommentDto.createDeletedCommentDto;
import static org.orury.domain.DomainFixtureFactory.TestCommentLikeDto.createCommentLikeDto;
import static org.orury.domain.DomainFixtureFactory.TestPostDto.createPostDto;

@DisplayName("[Facade] 댓글 Facade 테스트")
class CommentFacadeTest extends FacadeTest {

    @DisplayName("댓글생성Request와 유저id를 받으면, 댓글을 생성한다.")
    @Test
    void should_CreateComment() {
        // given
        CommentCreateRequest commentCreateRequest = createCommentCreateRequest().build().get();
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
        PostDto postDto = createPostDto().build().get();
        List<CommentDto> commentDtos = List.of(
                createCommentDto(24L).build().get(),
                createDeletedCommentDto().id(29L).build().get(),
                createCommentDto(31L).build().get()
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
        PostDto postDto = createPostDto().build().get();

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
        CommentDto commentDto = createCommentDto(commentId).build().get();
        CommentUpdateRequest commentUpdateRequest = createCommentUpdateRequest()
                .id(commentId).build().get();
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
        CommentDto commentDto = createCommentDto(commentId).build().get();
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
        CommentLikeDto commentLikeDto = createCommentLikeDto(commentId, userId).build().get();

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
        CommentLikeDto commentLikeDto = createCommentLikeDto(commentId, userId).build().get();

        // when
        commentFacade.deleteCommentLike(commentLikeDto);

        // then
        then(commentService).should(times(1))
                .deleteCommentLike(any());
    }
}
