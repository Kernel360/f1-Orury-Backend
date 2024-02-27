package org.orury.domain.comment.infrastructure;

import lombok.RequiredArgsConstructor;
import org.orury.domain.comment.domain.CommentStore;
import org.orury.domain.comment.domain.entity.Comment;
import org.orury.domain.comment.domain.entity.CommentLike;
import org.orury.domain.post.infrastructure.PostRepository;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CommentStoreImpl implements CommentStore {
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final PostRepository postRepository;

    @Override
    public void createComment(Comment comment) {
        commentRepository.save(comment);
        postRepository.increaseCommentCount(comment.getPost().getId());
    }

    @Override
    public void updateComment(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Comment comment) {
        Comment deletingComment = comment.delete();
        commentRepository.save(deletingComment);
        commentLikeRepository.deleteByCommentLikePK_CommentId(deletingComment.getId());
        postRepository.decreaseCommentCount(deletingComment.getPost().getId());
    }

    @Override
    public void createCommentLike(CommentLike commentLike) {
        commentLikeRepository.save(commentLike);
        commentRepository.increaseLikeCount(commentLike.getCommentLikePK().getCommentId());
    }

    @Override
    public void deleteCommentLike(CommentLike commentLike) {
        commentLikeRepository.delete(commentLike);
        commentRepository.decreaseLikeCount(commentLike.getCommentLikePK().getCommentId());
    }

    @Override
    public void deleteCommentLikesByUserId(Long userId) {
        commentLikeRepository.findByCommentLikePK_UserId(userId).forEach(
                commentLike -> {
                    commentRepository.decreaseLikeCount(commentLike.getCommentLikePK().getCommentId());
                    commentLikeRepository.delete(commentLike);
                }
        );
    }
}
