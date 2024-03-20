package org.orury.client.comment.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.orury.client.config.ServiceTest;
import org.orury.common.error.code.CommentErrorCode;
import org.orury.common.error.exception.BusinessException;
import org.orury.domain.DomainFixtureFactory;
import org.orury.domain.comment.domain.dto.CommentDto;
import org.orury.domain.comment.domain.dto.CommentLikeDto;
import org.orury.domain.comment.domain.entity.Comment;
import org.orury.domain.comment.domain.entity.CommentLikePK;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.post.domain.dto.PostDto;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.orury.domain.DomainFixtureFactory.TestComment.*;
import static org.orury.domain.DomainFixtureFactory.TestCommentDto.createChildCommentDto;
import static org.orury.domain.DomainFixtureFactory.TestCommentDto.createParentCommentDto;
import static org.orury.domain.DomainFixtureFactory.TestCommentLikeDto.createCommentLikeDto;
import static org.orury.domain.DomainFixtureFactory.TestCommentLikePK.createCommentLikePK;
import static org.orury.domain.DomainFixtureFactory.TestPostDto.createPostDto;
import static org.orury.domain.DomainFixtureFactory.TestUserDto.createUserDto;

@DisplayName("[Service] 댓글 ServiceImpl 테스트")
class CommentServiceImplTest extends ServiceTest {

    @Test
    @DisplayName("부모댓글id로 0을 가진 댓글Dto가 들어오면, 댓글이 생성되고 게시글의 댓글수가 증가되어야 한다.")
    void when_CommentIdOfCommentDtoEqualsToZero_Then_createComment() {
        // given
        CommentDto commentDto = createParentCommentDto().build().get();

        // when
        commentService.createComment(commentDto);

        // then
        then(commentReader).should(never())
                .findCommentById(anyLong());
        then(commentStore).should(times(1))
                .createComment(any());
    }

    @Test
    @DisplayName("유효한 부모댓글id을 가진 댓글Dto가 들어오면, 대댓글이 생성되고 게시글의 댓글수가 증가되어야 한다.")
    void when_CommentDtoWithValidParentCommentId_Then_createComment() {
        // given
        Long parentCommentId = 11L;
        Comment parentComment = createParentComment().build().get();
        CommentDto commentDto = createChildCommentDto(parentCommentId).build().get();

        given(commentReader.findCommentById(parentCommentId))
                .willReturn(Optional.of(parentComment));

        // when
        commentService.createComment(commentDto);

        // then
        then(commentReader).should(times(1))
                .findCommentById(anyLong());
        then(commentStore).should(times(1))
                .createComment(any());
    }

    @Test
    @DisplayName("댓글의 부모댓글id로 조회한 댓글이 존재하지 않으면, NotFound 예외를 발생시킨다.")
    void when_NotExistingParentCommentIdOfCommentDto_Then_NotFoundException() {
        // given
        Long parentCommentId = 12L;
        CommentDto commentDto = createChildCommentDto(parentCommentId).build().get();

        given(commentReader.findCommentById(parentCommentId))
                .willReturn(Optional.empty());

        // when & then
        assertThrows(BusinessException.class,
                () -> commentService.createComment(commentDto));

        then(commentReader).should(times(1))
                .findCommentById(anyLong());
        then(commentStore).should(never())
                .createComment(any());
    }

    @Test
    @DisplayName("댓글의 부모댓글id로 조회한 댓글이 (댓글이 아닌) 대댓글이면, BadRequest 예외를 발생시킨다.")
    void when_ParentCommentIdOfCommentDtoIsIdOfChildComment_Then_BadRequestException() {
        // given
        Long parentCommentId = 11L;
        Comment childComment = createChildComment().build().get();
        CommentDto commentDto = createChildCommentDto(parentCommentId).build().get();

        given(commentReader.findCommentById(parentCommentId))
                .willReturn(Optional.of(childComment));

        // when & then
        assertThrows(BusinessException.class,
                () -> commentService.createComment(commentDto));

        then(commentReader).should(times(1))
                .findCommentById(anyLong());
        then(commentStore).should(never())
                .createComment(any());
    }

    @Test
    @DisplayName("게시글Dto와 cursor가 들어오면, (cursor기반으로 조회한) 해당 게시글의 댓글Dto 목록을 반환한다.")
    void when_PostDtoAndCursor_Then_RetrieveCommentDtos() {
        // given
        Long postId = 154L;
        PostDto postDto = createPostDto(postId).build().get();
        Long cursor = 22L;
        List<Comment> comments = List.of(
                createComment(1L).build().get(),
                createComment(2L).build().get(),
                createComment(3L).build().get()
        );

        given(commentReader.getCommentsByPostIdAndCursor(postId, cursor, PageRequest.of(0, NumberConstants.COMMENT_PAGINATION_SIZE))).willReturn(comments);
        // when
        commentService.getCommentDtosByPost(postDto, cursor);

        // then
        then(commentReader).should(times(1)).getCommentsByPostIdAndCursor(anyLong(), anyLong(), any());
    }

    @Test
    @DisplayName("(cursor기반으로 조회한) 해당 게시글의 댓글이 없으면, 빈리스트를 반환한다.")
    void when_EmptyCommentsForPostDtoAndCursor_Then_RetrieveEmptyList() {
        // given
        Long postId = 154L;
        PostDto postDto = createPostDto(postId).build().get();
        Long cursor = 22L;

        given(commentReader.getCommentsByPostIdAndCursor(postId, cursor, PageRequest.of(0, NumberConstants.COMMENT_PAGINATION_SIZE))).willReturn(Collections.emptyList());

        // when
        commentService.getCommentDtosByPost(postDto, cursor);

        // then
        then(commentReader).should(times(1)).getCommentsByPostIdAndCursor(anyLong(), anyLong(), any());
    }

    @Test
    @DisplayName("유저id와 cursor가 들어오면, (cursor기반으로 조회한) 해당 유저가 작성한 댓글Dto 목록을 반환한다.")
    void when_UserDtoAndCursor_Then_RetrieveCommentDtos() {
        // given
        Long userId = 154L;
        Long cursor = 22L;
        List<Comment> comments = List.of(
                createComment(1L).build().get(),
                createComment(2L).build().get(),
                createComment(3L).build().get()
        );

        given(commentReader.getCommentsByUserIdAndCursor(userId, cursor, PageRequest.of(0, NumberConstants.COMMENT_PAGINATION_SIZE))).willReturn(comments);

        // when
        commentService.getCommentDtosByUserId(userId, cursor);

        // then
        then(commentReader).should(times(1))
                .getCommentsByUserIdAndCursor(anyLong(), anyLong(), any());
    }

    @Test
    @DisplayName("(cursor기반으로 조회한) 해당 유저가 작성한 댓글이 없으면, 빈리스트를 반환한다.")
    void when_EmptyCommentsForUserDtoAndCursor_Then_RetrieveEmptyList() {
        // given
        Long userId = 154L;
        Long cursor = 22L;

        given(commentReader.getCommentsByUserIdAndCursor(userId, cursor, PageRequest.of(0, NumberConstants.COMMENT_PAGINATION_SIZE)))
                .willReturn(Collections.emptyList());

        // when
        commentService.getCommentDtosByUserId(userId, cursor);

        // then
        then(commentReader).should(times(1))
                .getCommentsByUserIdAndCursor(anyLong(), anyLong(), any());
    }

    @Test
    @DisplayName("수정하려는 댓글Dto의 작성자가 유저Id의 유저라면, 댓글을 수정한다.")
    void when_UserIdEqualsToCommentCreatorId_Then_UpdateComment() {
        // given
        Long userId = 4L;
        CommentDto commentDto = createCommentDtoWithUserId(userId);

        // when
        commentService.updateComment(commentDto, userId);

        // then
        then(commentStore).should(times(1))
                .updateComment(any());
    }

    @Test
    @DisplayName("수정하려는 댓글Dto의 작성자가 유저Id의 유저가 아니면, Forbidden 예외를 발생시킨다.")
    void when_UserIdNotEqualsToCommentCreatorId_Then_ForbiddenException() {
        // given
        Long commentCreatorId = 4L;
        CommentDto commentDto = createCommentDtoWithUserId(commentCreatorId);
        Long userId = 142L;

        // when & then
        assertThrows(BusinessException.class,
                () -> commentService.updateComment(commentDto, userId));

        then(commentStore).should(never())
                .updateComment(any());
    }

    @Test
    @DisplayName("삭제하려는 댓글Dto의 작성자가 유저Id의 유저라면, 댓글을 삭제한다.")
    void when_UserIdEqualsToCommentCreatorId_Then_DeleteComment() {
        // given
        Long userId = 4L;
        CommentDto commentDto = createCommentDtoWithUserId(userId);

        // when
        commentService.deleteComment(commentDto, userId);

        // then
        then(commentStore).should(times(1))
                .deleteComment(any());
    }

    @Test
    @DisplayName("삭제하려는 댓글Dto의 작성자가 유저Id의 유저가 아니면, Forbidden 예외를 발생시킨다.")
    void when_UserIdIsNotCommentCreatorId_Then_ForbiddenException() {
        // given
        Long commentCreatorId = 4L;
        CommentDto commentDto = createCommentDtoWithUserId(commentCreatorId);
        Long userId = 142L;

        // when & then
        assertThrows(BusinessException.class,
                () -> commentService.deleteComment(commentDto, userId));

        then(commentStore).should(never())
                .deleteComment(any());
    }

    @Test
    @DisplayName("존재하는 댓글id가 들어오면, 정상적으로 댓글Dto를 반환한다.")
    void should_RetrieveCommentDtoById() {
        // given
        Long commentId = 1L;
        Comment comment = createComment(commentId).build().get();

        given(commentReader.findCommentById(commentId))
                .willReturn(Optional.of(comment));

        CommentDto expectedCommentDto = CommentDto.from(comment);

        // when
        CommentDto actualCommentDto = commentService.getCommentDtoById(commentId);

        // then
        assertEquals(expectedCommentDto, actualCommentDto);

        then(commentReader).should(times(1))
                .findCommentById(anyLong());
    }

    @Test
    @DisplayName("존재하지 않는 댓글id가 들어오면, NOT_FOUND 예외를 반환한다.")
    void when_NotExistingCommentId_Then_NotFoundException() {
        // given
        Long commentId = 1L;

        given(commentReader.findCommentById(commentId))
                .willReturn(Optional.empty());

        // when & then
        assertThrows(BusinessException.class,
                () -> commentService.getCommentDtoById(commentId));

        then(commentReader).should(times(1))
                .findCommentById(anyLong());
    }

    @Test
    @DisplayName("댓글에 대한 유저의 댓글좋아요가 기존에 없다면, 정상적으로 댓글 좋아요를 생성한다.")
    void should_CreateCommentLike() {
        // given
        CommentLikePK commentLikePK = createCommentLikePK().build().get();
        Comment comment = createComment().build().get();
        CommentLikeDto commentLikeDto = createCommentLikeDto(commentLikePK).build().get();

        given(commentReader.findCommentById(anyLong()))
                .willReturn(Optional.of(comment));
        given(commentReader.existsCommentLikeById(commentLikePK))
                .willReturn(false);

        // when
        commentService.createCommentLike(commentLikeDto);

        // then
        then(commentReader).should(times(1))
                .findCommentById(anyLong());
        then(commentReader).should(times(1))
                .existsCommentLikeById(any());
        then(commentStore).should(times(1))
                .createCommentLike(any());
    }

    @Test
    @DisplayName("댓글에 대한 유저의 댓글좋아요가 기존에 있다면, 생성하지 않고 return한다.")
    void when_AlreadyExistingCommentLike_Then_ReturnWithoutSave() {
        // given
        CommentLikePK commentLikePK = createCommentLikePK().build().get();
        Comment comment = createComment().build().get();
        CommentLikeDto commentLikeDto = createCommentLikeDto(commentLikePK).build().get();

        given(commentReader.findCommentById(anyLong()))
                .willReturn(Optional.of(comment));
        given(commentReader.existsCommentLikeById(commentLikePK))
                .willReturn(true);

        // when
        commentService.createCommentLike(commentLikeDto);

        // then
        then(commentReader).should(times(1))
                .findCommentById(anyLong());
        then(commentReader).should(times(1))
                .existsCommentLikeById(any());
        then(commentStore).should(never())
                .createCommentLike(any());
    }

    @Test
    @DisplayName("존재하지 않는 댓글에 대한 댓글좋아요 생성 요청이 들어온다면, NotFound 예외를 반환한다.")
    void when_AttemptToCreateCommentLikeForNotExistingComment_Then_NotFoundException() {
        // given
        CommentLikePK commentLikePK = createCommentLikePK().build().get();
        CommentLikeDto commentLikeDto = createCommentLikeDto(commentLikePK).build().get();

        given(commentReader.findCommentById(anyLong()))
                .willReturn(Optional.empty());

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> commentService.createCommentLike(commentLikeDto));

        assertEquals(CommentErrorCode.NOT_FOUND.getMessage(), exception.getMessage());

        then(commentReader).should(times(1))
                .findCommentById(anyLong());
        then(commentReader).should(never())
                .existsCommentLikeById(any());
        then(commentStore).should(never())
                .createCommentLike(any());
    }

    @Test
    @DisplayName("존재하지만 삭제처리된 댓글에 대한 댓글좋아요 생성 요청이 들어온다면, Forbidden 예외를 반환한다.")
    void when_AttemptToCreateCommentLikeForDeletedComment_Then_ForbiddenException() {
        // given
        CommentLikePK commentLikePK = createCommentLikePK().build().get();
        Comment deletedComment = createDeletedComment().build().get();
        CommentLikeDto commentLikeDto = createCommentLikeDto(commentLikePK).build().get();

        given(commentReader.findCommentById(anyLong()))
                .willReturn(Optional.of(deletedComment));

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> commentService.createCommentLike(commentLikeDto));

        assertEquals(CommentErrorCode.FORBIDDEN.getMessage(), exception.getMessage());

        then(commentReader).should(times(1))
                .findCommentById(anyLong());
        then(commentReader).should(never())
                .existsCommentLikeById(any());
        then(commentStore).should(never())
                .createCommentLike(any());
    }

    @Test
    @DisplayName("댓글에 대한 유저의 댓글좋아요가 기존에 있다면, 정상적으로 댓글 좋아요를 삭제한다.")
    void should_DeleteCommentLike() {
        // given
        CommentLikePK commentLikePK = createCommentLikePK().build().get();
        Comment comment = createComment().build().get();
        CommentLikeDto commentLikeDto = createCommentLikeDto(commentLikePK).build().get();

        given(commentReader.findCommentById(anyLong()))
                .willReturn(Optional.of(comment));
        given(commentReader.existsCommentLikeById(commentLikePK))
                .willReturn(true);

        // when
        commentService.deleteCommentLike(commentLikeDto);

        // then
        then(commentReader).should(times(1))
                .findCommentById(anyLong());
        then(commentReader).should(times(1))
                .existsCommentLikeById(any());
        then(commentStore).should(times(1))
                .deleteCommentLike(any());
    }

    @Test
    @DisplayName("댓글에 대한 유저의 댓글좋아요가 기존에 없다면, 삭제하지 않고 return한다.")
    void when_AlreadyNotExistingCommentLike_Then_ReturnWithoutDelete() {
        // given
        CommentLikePK commentLikePK = createCommentLikePK().build().get();
        Comment comment = createComment().build().get();
        CommentLikeDto commentLikeDto = createCommentLikeDto(commentLikePK).build().get();

        given(commentReader.findCommentById(anyLong()))
                .willReturn(Optional.of(comment));
        given(commentReader.existsCommentLikeById(commentLikePK))
                .willReturn(false);

        // when
        commentService.deleteCommentLike(commentLikeDto);

        // then
        then(commentReader).should(times(1))
                .findCommentById(anyLong());
        then(commentReader).should(times(1))
                .existsCommentLikeById(any());
        then(commentStore).should(never())
                .deleteCommentLike(any());
    }

    @Test
    @DisplayName("존재하지 않는 댓글에 대한 댓글좋아요 삭제 요청이 들어온다면, NotFound 예외를 반환한다.")
    void when_AttemptToDeleteCommentLikeForNotExistingComment_Then_NotFoundException() {
        // given
        CommentLikePK commentLikePK = createCommentLikePK().build().get();
        CommentLikeDto commentLikeDto = createCommentLikeDto(commentLikePK).build().get();

        given(commentReader.findCommentById(anyLong()))
                .willReturn(Optional.empty());

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> commentService.deleteCommentLike(commentLikeDto));

        assertEquals(CommentErrorCode.NOT_FOUND.getMessage(), exception.getMessage());

        then(commentReader).should(times(1))
                .findCommentById(anyLong());
        then(commentReader).should(never())
                .existsCommentLikeById(any());
        then(commentStore).should(never())
                .deleteCommentLike(any());
    }

    @Test
    @DisplayName("존재하지만 삭제처리된 댓글에 대한 댓글좋아요 삭제 요청이 들어온다면, Forbidden 예외를 반환한다.")
    void when_AttemptToDeleteCommentLikeForDeletedComment_Then_ForbiddenException() {
        // given
        CommentLikePK commentLikePK = createCommentLikePK().build().get();
        Comment deletedComment = createDeletedComment().build().get();
        CommentLikeDto commentLikeDto = createCommentLikeDto(commentLikePK).build().get();

        given(commentReader.findCommentById(anyLong()))
                .willReturn(Optional.of(deletedComment));

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> commentService.deleteCommentLike(commentLikeDto));

        assertEquals(CommentErrorCode.FORBIDDEN.getMessage(), exception.getMessage());

        then(commentReader).should(times(1))
                .findCommentById(anyLong());
        then(commentReader).should(never())
                .existsCommentLikeById(any());
        then(commentStore).should(never())
                .deleteCommentLike(any());
    }

    @Test
    @DisplayName("유저id와 댓글id에 대해 좋아요가 존재하면, true를 반환한다.")
    void when_IdOfExitingCommentLike_Then_ReturnTrue() {
        // given
        Long userId = 1L;
        Long commentId = 2L;

        given(commentReader.existsCommentLikeByUserIdAndCommentId(userId, commentId))
                .willReturn(true);

        // when & then
        assertTrue(commentService.isLiked(userId, commentId));

        then(commentReader).should(times(1))
                .existsCommentLikeByUserIdAndCommentId(anyLong(), anyLong());
    }

    @Test
    @DisplayName("유저id와 댓글id에 대해 좋아요가 존재하지 않으면, false를 반환한다.")
    void when_IdOfNotExitingCommentLike_Then_ReturnFalse() {
        //given
        Long userId = 1L;
        Long commentId = 2L;

        given(commentReader.existsCommentLikeByUserIdAndCommentId(userId, commentId))
                .willReturn(false);

        // when & then
        assertFalse(commentService.isLiked(userId, commentId));

        then(commentReader).should(times(1))
                .existsCommentLikeByUserIdAndCommentId(anyLong(), anyLong());
    }

    private CommentDto createCommentDtoWithUserId(Long userId) {
        return DomainFixtureFactory.TestCommentDto.createCommentDto()
                .userDto(createUserDto(userId).build().get()).build().get();
    }
}
