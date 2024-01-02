package org.fastcampus.oruryclient.comment.service;

import org.fastcampus.oruryclient.comment.error.CommentErrorCode;
import org.fastcampus.oruryclient.global.constants.NumberConstants;
import org.fastcampus.oruryclient.global.error.BusinessException;
import org.fastcampus.orurydomain.comment.db.model.Comment;
import org.fastcampus.orurydomain.comment.db.model.CommentLike;
import org.fastcampus.orurydomain.comment.db.model.CommentLikePK;
import org.fastcampus.orurydomain.comment.db.repository.CommentLikeRepository;
import org.fastcampus.orurydomain.comment.db.repository.CommentRepository;
import org.fastcampus.orurydomain.comment.dto.CommentLikeDto;
import org.fastcampus.orurydomain.post.db.model.Post;
import org.fastcampus.orurydomain.user.db.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
@DisplayName("CommentLikeServiceTest")
@ActiveProfiles("test")
class CommentLikeServiceTest {

    private CommentLikeRepository commentLikeRepository;
    private CommentRepository commentRepository;
    private CommentLikeService commentLikeService;

    @BeforeEach
    void setUp() {
        commentLikeRepository = mock(CommentLikeRepository.class);
        commentRepository = mock(CommentRepository.class);
        commentLikeService = new CommentLikeService(commentLikeRepository, commentRepository);
    }

    @Test
    @DisplayName("댓글에 좋아요를 누르면, DB에 데이터가 생성되고 댓글에 좋아요 개수를 증가시킨다.")
    void when_LikeComment_Then_CreateCommentLikeAndIncreaseCommentLikeCount() {
        // given
        CommentLikePK commentLikePK = createCommentLikePK();
        CommentLike commentLike = createCommentLike(commentLikePK);
        Comment comment = createComment();

        when(commentRepository.findById(anyLong()))
                .thenReturn(Optional.of(comment));
        when(commentLikeRepository.existsById(any(CommentLikePK.class)))
                .thenReturn(false);

        // when
        commentLikeService.createCommentLike(CommentLikeDto.from(commentLike));

        // then
        verify(commentRepository, times(1))
                .findById(commentLikePK.getCommentId());
        verify(commentLikeRepository, times(1))
                .existsById(commentLikePK);
        verify(commentLikeRepository, times(1))
                .save(commentLike);
        verify(commentRepository, times(1))
                .increaseLikeCount(commentLikePK.getCommentId());
    }

    @Test
    @DisplayName("좋아요 시 댓글이 존재하지 않으면, NOT_FOUND 예외를 발생시킨다.")
    void verify_LikeNotExistingComment_Then_NotFoundException() {
        // given
        CommentLikePK commentLikePK = createCommentLikePK();
        CommentLike commentLike = createCommentLike(commentLikePK);

        when(commentRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // when & then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> commentLikeService.createCommentLike(CommentLikeDto.from(commentLike)));

        assertEquals(CommentErrorCode.NOT_FOUND, exception.getErrorCode());

        verify(commentRepository, times(1))
                .findById(commentLikePK.getCommentId());
        verify(commentLikeRepository, never())
                .existsById(commentLikePK);
        verify(commentLikeRepository, never())
                .save(commentLike);
        verify(commentRepository, never())
                .increaseLikeCount(commentLikePK.getCommentId());
    }

    @Test
    @DisplayName("좋아요 시 기존에 눌렀던 댓글에 대한 좋아요면, DB에 새로 생성/수정하지 않고 댓글 좋아요 수도 변경하지 않는다.")
    void when_LikeAlreadyLikedComment_Then_NotChangeCommentLikeAndNotChangeCommentLikeCount() {
        // given
        CommentLikePK commentLikePK = createCommentLikePK();
        CommentLike commentLike = createCommentLike(commentLikePK);
        Comment comment = createComment();

        when(commentRepository.findById(anyLong()))
                .thenReturn(Optional.of(comment));
        when(commentLikeRepository.existsById(any(CommentLikePK.class)))
                .thenReturn(true);

        // when
        commentLikeService.createCommentLike(CommentLikeDto.from(commentLike));

        // then
        verify(commentRepository, times(1))
                .findById(commentLikePK.getCommentId());
        verify(commentLikeRepository, times(1))
                .existsById(commentLikePK);
        verify(commentLikeRepository, never())
                .save(commentLike);
        verify(commentRepository, never())
                .increaseLikeCount(commentLikePK.getCommentId());
    }

    @Test
    @DisplayName("댓글에 좋아요 취소를 누르면, DB에 데이터가 삭제되고 댓글에 좋아요 개수를 감소시킨다.")
    void when_CancelCommentLike_ThenDeleteCommentLikeAndDecreaseCommentLikeCount() {
        // given
        CommentLikePK commentLikePK = createCommentLikePK();
        CommentLike commentLike = createCommentLike(commentLikePK);
        Comment comment = createComment();

        when(commentRepository.findById(anyLong()))
                .thenReturn(Optional.of(comment));
        when(commentLikeRepository.existsById(any(CommentLikePK.class)))
                .thenReturn(true);

        // when
        commentLikeService.deleteCommentLike(CommentLikeDto.from(commentLike));

        // then
        verify(commentRepository, times(1))
                .findById(commentLikePK.getCommentId());
        verify(commentLikeRepository, times(1))
                .existsById(commentLikePK);
        verify(commentLikeRepository, times(1))
                .delete(commentLike);
        verify(commentRepository, times(1))
                .decreaseLikeCount(commentLikePK.getCommentId());
    }

    @Test
    @DisplayName("좋아요 취소 시 댓글이 존재하지 않으면, NOT_FOUND 예외를 발생시킨다.")
    void verify_CancelLikeOfNotExistingComment_Then_NotFoundException() {
        // given
        CommentLikePK commentLikePK = createCommentLikePK();
        CommentLike commentLike = createCommentLike(commentLikePK);

        when(commentRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // when & then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> commentLikeService.deleteCommentLike(CommentLikeDto.from(commentLike)));

        assertEquals(CommentErrorCode.NOT_FOUND, exception.getErrorCode());

        verify(commentRepository, times(1))
                .findById(commentLikePK.getCommentId());
        verify(commentLikeRepository, never())
                .existsById(commentLikePK);
        verify(commentLikeRepository, never())
                .delete(commentLike);
        verify(commentRepository, never())
                .decreaseLikeCount(commentLikePK.getCommentId());
    }

    @Test
    @DisplayName("좋아요 취소 시 댓글에 대한 유저의 좋아요 정보가 없으면, DB에서 삭제하지 않고 댓글 좋아요 수 변경하지 않는다.")
    void when_CancelLikeNotExistingLikedComment_Then_NotDeleteCommentLikeAndNotChangeCommentLikeCount() {
        // given
        CommentLikePK commentLikePK = createCommentLikePK();
        CommentLike commentLike = createCommentLike(commentLikePK);
        Comment comment = createComment();

        when(commentRepository.findById(anyLong()))
                .thenReturn(Optional.of(comment));
        when(commentLikeRepository.existsById(any(CommentLikePK.class)))
                .thenReturn(false);

        // when
        commentLikeService.deleteCommentLike(CommentLikeDto.from(commentLike));

        // then
        verify(commentRepository, times(1))
                .findById(commentLikePK.getCommentId());
        verify(commentLikeRepository, times(1))
                .existsById(commentLikePK);
        verify(commentLikeRepository, never())
                .delete(commentLike);
        verify(commentRepository, never())
                .decreaseLikeCount(commentLikePK.getCommentId());
    }

    @Test
    @DisplayName("유저가 해당 댓글에 좋아요 눌렀는지 확인해야 한다.")
    void should_CheckWhetherUserClickedCommentLike() {
        // given
        Long userId = 1L;
        Long commentId = 1L;

        // when
        commentLikeService.isLiked(userId, commentId);

        // then
        verify(commentLikeRepository, times(1))
                .existsCommentLikeByCommentLikePK_UserIdAndCommentLikePK_CommentId(userId, commentId);
    }

    private static User createUser() {
        return User.of(
                1L,
                "userEmail",
                "userNickname",
                "userPassword",
                1,
                1,
                LocalDate.now(),
                "userProfileImage",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    private static Post createPost() {
        return Post.of(
                1L,
                "postTitle",
                "postContent",
                0,
                0,
                0,
                "postImage",
                1,
                createUser(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    private static Comment createComment() {
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

    private static CommentLikePK createCommentLikePK() {
        return CommentLikePK.of(1L, 1L);
    }

    private static CommentLike createCommentLike(CommentLikePK commentLikePK) {
        return CommentLike.of(commentLikePK);
    }
}
