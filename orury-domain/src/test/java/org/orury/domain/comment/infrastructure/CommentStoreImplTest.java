package org.orury.domain.comment.infrastructure;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.orury.domain.comment.domain.entity.Comment;
import org.orury.domain.comment.domain.entity.CommentLike;
import org.orury.domain.config.InfrastructureTest;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.post.domain.entity.Post;
import org.orury.domain.user.domain.dto.UserStatus;
import org.orury.domain.user.domain.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.orury.domain.DomainFixtureFactory.TestCommentLike.createCommentLike;

@DisplayName("[Store] 댓글 StoreImpl 테스트")
class CommentStoreImplTest extends InfrastructureTest {

    @Test
    @DisplayName("댓글 생성 시, 댓글을 저장하고 게시글의 댓글 수를 증가시켜야 한다.")
    void when_CreateComment_Then_SaveCommentAndIncreaseCommentLikeCount() {
        // given
        Comment comment = createComment();

        // when
        commentStore.createComment(comment);

        // then
        then(commentRepository).should(times(1))
                .save(any());
        then(postRepository).should(times(1))
                .increaseCommentCount(anyLong());
    }

    @Test
    @DisplayName("댓글 수정 시, 댓글을 저장해야 한다.")
    void when_UpdateComment_Then_SaveComment() {
        // given
        Comment comment = createComment();

        // when
        commentStore.updateComment(comment);

        // then
        then(commentRepository).should(times(1))
                .save(any());
    }

    @Test
    @DisplayName("댓글 삭제 시, 댓글 삭제처리 및 저장하고, 댓글에 달린 좋아요를 삭제하고, 게시글의 댓글 수를 감소시켜야 한다.")
    void when_DeleteComment_Then_DeleteCommentAndDeleteCommentLikesAndDecreaseCommentCountsOfPost() {
        // given
        Long commentId = 2L;
        Comment comment = createComment(commentId);

        // when
        commentStore.deleteComment(comment);

        // then
        then(commentRepository).should(times(1))
                .save(any());
        then(commentLikeRepository).should(times(1))
                .deleteByCommentLikePK_CommentId(anyLong());
        then(postRepository).should(times(1))
                .decreaseCommentCount(anyLong());
    }

    @Test
    @DisplayName("성공적으로 CommentLike를 생성하고 Comment의 likeCount를 늘려야 한다.")
    void should_CreateCommentLikeAndIncreaseCommentLIkeCount() {
        // given
        CommentLike commentLike = createCommentLike().build().get();

        // when
        commentStore.createCommentLike(commentLike);

        // then
        then(commentLikeRepository).should(times(1))
                .save(any());
        then(commentRepository).should(times(1))
                .increaseLikeCount(anyLong());
    }

    @Test
    @DisplayName("성공적으로 CommentLike를 삭제하고 Comment의 likeCount를 줄여야 한다.")
    void should_DeleteCommentLikeAndDecreaseCommentLIkeCount() {
        // given
        CommentLike commentLike = createCommentLike().build().get();

        // when
        commentStore.deleteCommentLike(commentLike);

        // then
        then(commentLikeRepository).should(times(1))
                .delete(any());
        then(commentRepository).should(times(1))
                .decreaseLikeCount(anyLong());
    }

    @Test
    @DisplayName("유저id를 받아, 해당 유저가 누른 댓글 좋아요를 모두 삭제하고, 관련 댓글의 좋아요 수를 줄여야 한다.")
    void when_UserId_Then_DeleteCommentLikesByUserAndDecreaseCommentLikeCount() {
        // given
        Long userId = 4L;
        List<CommentLike> commentLikes = List.of(
                createCommentLike(1L, userId).build().get(),
                createCommentLike(2L, userId).build().get(),
                createCommentLike(3L, userId).build().get()
        );

        given(commentLikeRepository.findByCommentLikePK_UserId(userId))
                .willReturn(commentLikes);

        // when
        commentStore.deleteCommentLikesByUserId(userId);

        // then
        then(commentLikeRepository).should(times(1))
                .findByCommentLikePK_UserId(anyLong());
        then(commentRepository).should(times(commentLikes.size()))
                .decreaseLikeCount(anyLong());
        then(commentLikeRepository).should(times(commentLikes.size()))
                .delete(any());
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
                NumberConstants.PARENT_COMMENT,
                0,
                createPost(),
                createUser(),
                NumberConstants.IS_NOT_DELETED,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    private Comment createComment(Long commentId) {
        return Comment.of(
                commentId,
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
}

