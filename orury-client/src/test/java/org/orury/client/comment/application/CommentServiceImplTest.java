package org.orury.client.comment.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.orury.common.error.code.CommentErrorCode;
import org.orury.common.error.exception.BusinessException;
import org.orury.domain.comment.domain.CommentReader;
import org.orury.domain.comment.domain.CommentStore;
import org.orury.domain.comment.domain.dto.CommentDto;
import org.orury.domain.comment.domain.dto.CommentLikeDto;
import org.orury.domain.comment.domain.entity.Comment;
import org.orury.domain.comment.domain.entity.CommentLikePK;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.global.image.ImageReader;
import org.orury.domain.post.domain.dto.PostDto;
import org.orury.domain.post.domain.entity.Post;
import org.orury.domain.user.domain.dto.UserDto;
import org.orury.domain.user.domain.dto.UserStatus;
import org.orury.domain.user.domain.entity.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Service] 댓글 ServiceImpl 테스트")
@ActiveProfiles("test")
class CommentServiceImplTest {
    private CommentService commentService;
    private CommentReader commentReader;
    private CommentStore commentStore;
    private ImageReader imageReader;

    @BeforeEach
    void setUp() {
        commentReader = mock(CommentReader.class);
        commentStore = mock(CommentStore.class);
        imageReader = mock(ImageReader.class);

        commentService = new CommentServiceImpl(commentReader, commentStore, imageReader);
    }

    @Test
    @DisplayName("부모댓글id로 0을 가진 댓글Dto가 들어오면, 댓글이 생성되고 게시글의 댓글수가 증가되어야 한다.")
    void when_CommentIdOfCommentDtoEqualsToZero_Then_createComment() {
        // given
        Long parentCommentId = NumberConstants.PARENT_COMMENT;
        CommentDto commentDto = createCommentDto(parentCommentId);

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
        Comment parentComment = createParentComment();
        CommentDto commentDto = createCommentDto(parentCommentId);

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
        CommentDto commentDto = createCommentDto(parentCommentId);

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
        Comment childComment = createChildComment();
        CommentDto commentDto = createCommentDto(parentCommentId);

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
        PostDto postDto = createPostDto(postId);
        Long cursor = 22L;
        String profileImage = "profile.png";
        List<Comment> comments = List.of(
                createComment(1L),
                createComment(2L),
                createComment(3L)
        );

        given(commentReader.getCommentsByPostIdAndCursor(postId, cursor, PageRequest.of(0, NumberConstants.COMMENT_PAGINATION_SIZE)))
                .willReturn(comments);
        given(imageReader.getUserImageLink(anyString()))
                .willReturn(profileImage);

        // when
        commentService.getCommentDtosByPost(postDto, cursor);

        // then
        then(commentReader).should(times(1))
                .getCommentsByPostIdAndCursor(anyLong(), anyLong(), any());
        then(imageReader).should(times(comments.size()))
                .getUserImageLink(anyString());
    }

    @Test
    @DisplayName("(cursor기반으로 조회한) 해당 게시글의 댓글이 없으면, 빈리스트를 반환한다.")
    void when_EmptyCommentsForPostDtoAndCursor_Then_RetrieveEmptyList() {
        // given
        Long postId = 154L;
        PostDto postDto = createPostDto(postId);
        Long cursor = 22L;

        given(commentReader.getCommentsByPostIdAndCursor(postId, cursor, PageRequest.of(0, NumberConstants.COMMENT_PAGINATION_SIZE)))
                .willReturn(Collections.emptyList());

        // when
        commentService.getCommentDtosByPost(postDto, cursor);

        // then
        then(commentReader).should(times(1))
                .getCommentsByPostIdAndCursor(anyLong(), anyLong(), any());
        then(imageReader).should(never())
                .getUserImageLink(anyString());
    }

    @Test
    @DisplayName("유저id와 cursor가 들어오면, (cursor기반으로 조회한) 해당 유저가 작성한 댓글Dto 목록을 반환한다.")
    void when_UserDtoAndCursor_Then_RetrieveCommentDtos() {
        // given
        Long userId = 154L;
        Long cursor = 22L;
        String profileImage = "profile.png";
        List<Comment> comments = List.of(
                createComment(1L),
                createComment(2L),
                createComment(3L)
        );

        given(commentReader.getCommentsByUserIdAndCursor(userId, cursor, PageRequest.of(0, NumberConstants.COMMENT_PAGINATION_SIZE)))
                .willReturn(comments);
        given(imageReader.getUserImageLink(anyString()))
                .willReturn(profileImage);

        // when
        commentService.getCommentDtosByUserId(userId, cursor);

        // then
        then(commentReader).should(times(1))
                .getCommentsByUserIdAndCursor(anyLong(), anyLong(), any());
        then(imageReader).should(times(comments.size()))
                .getUserImageLink(anyString());
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
        then(imageReader).should(never())
                .getUserImageLink(anyString());
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
        Comment comment = createComment(commentId);

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
        CommentLikePK commentLikePK = createCommentLikePK();
        Comment comment = createComment();
        CommentLikeDto commentLikeDto = createCommentLikeDto(commentLikePK);

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
        CommentLikePK commentLikePK = createCommentLikePK();
        Comment comment = createComment();
        CommentLikeDto commentLikeDto = createCommentLikeDto(commentLikePK);

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
        CommentLikePK commentLikePK = createCommentLikePK();
        CommentLikeDto commentLikeDto = createCommentLikeDto(commentLikePK);

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
        CommentLikePK commentLikePK = createCommentLikePK();
        Comment deletedComment = createDeletedComment();
        CommentLikeDto commentLikeDto = createCommentLikeDto(commentLikePK);

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
        CommentLikePK commentLikePK = createCommentLikePK();
        Comment comment = createComment();
        CommentLikeDto commentLikeDto = createCommentLikeDto(commentLikePK);

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
        CommentLikePK commentLikePK = createCommentLikePK();
        Comment comment = createComment();
        CommentLikeDto commentLikeDto = createCommentLikeDto(commentLikePK);

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
        CommentLikePK commentLikePK = createCommentLikePK();
        CommentLikeDto commentLikeDto = createCommentLikeDto(commentLikePK);

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
        CommentLikePK commentLikePK = createCommentLikePK();
        Comment deletedComment = createDeletedComment();
        CommentLikeDto commentLikeDto = createCommentLikeDto(commentLikePK);

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

    private UserDto createUserDto(Long userId) {
        return UserDto.of(
                userId,
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

    private PostDto createPostDto(Long postId) {
        return PostDto.of(
                postId,
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

    private CommentDto createCommentDto(Long parentId) {
        return CommentDto.of(
                1L,
                "commentContent",
                parentId,
                0,
                createPostDto(),
                createUserDto(),
                NumberConstants.IS_NOT_DELETED,
                LocalDateTime.of(2024, 1, 1, 11, 50),
                LocalDateTime.of(2024, 1, 1, 11, 50)
        );
    }

    private CommentDto createCommentDtoWithUserId(Long userId) {
        return CommentDto.of(
                1L,
                "commentContent",
                12L,
                0,
                createPostDto(),
                createUserDto(userId),
                NumberConstants.IS_NOT_DELETED,
                LocalDateTime.of(2024, 1, 1, 11, 50),
                LocalDateTime.of(2024, 1, 1, 11, 50)
        );
    }

    private User createUser() {
        return User.of(
                1L,
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

    private Post createPost() {
        return Post.of(
                1L,
                "postTitle",
                "postContent",
                0,
                0,
                0,
                List.of(),
                1,
                createUser(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    private Comment createComment() {
        return Comment.of(
                1L,
                "commentContent",
                12L,
                0,
                createPost(),
                createUser(),
                NumberConstants.IS_NOT_DELETED,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    private Comment createDeletedComment() {
        return Comment.of(
                1L,
                "commentContent",
                12L,
                0,
                createPost(),
                createUser(),
                NumberConstants.IS_DELETED,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    private Comment createComment(Long commentId) {
        return Comment.of(
                commentId,
                "commentContent",
                12L,
                0,
                createPost(),
                createUser(),
                NumberConstants.IS_NOT_DELETED,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    private Comment createParentComment() {
        return Comment.of(
                1L,
                "commentContent",
                NumberConstants.PARENT_COMMENT,
                0,
                createPost(),
                createUser(),
                NumberConstants.IS_NOT_DELETED,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    private Comment createChildComment() {
        return Comment.of(
                1L,
                "commentContent",
                67L,
                0,
                createPost(),
                createUser(),
                NumberConstants.IS_NOT_DELETED,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    private CommentLikePK createCommentLikePK() {
        return CommentLikePK.of(2L, 1L);
    }

    private CommentLikeDto createCommentLikeDto(CommentLikePK commentLikePK) {
        return CommentLikeDto.of(commentLikePK);
    }
}
