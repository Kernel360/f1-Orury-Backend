package org.orury.domain.comment.domain;

import org.orury.domain.comment.domain.entity.Comment;
import org.orury.domain.comment.domain.entity.CommentLikePK;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface CommentReader {
    Comment getCommentById(Long commentId);

    boolean existsCommentById(Long commentId);

    boolean existsCommentLikeById(CommentLikePK commentLikePK);

    boolean existsCommentLikeByUserIdAndCommentId(Long userId, Long commentId);

    List<Comment> getCommentsByPostIdAndCursor(Long postId, Long cursor, PageRequest pageRequest);

    List<Comment> getCommentsByUserIdAndCursor(Long userId, Long cursor, PageRequest pageRequest);
}
