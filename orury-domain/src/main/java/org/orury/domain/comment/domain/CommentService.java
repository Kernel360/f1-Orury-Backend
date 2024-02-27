package org.orury.domain.comment.domain;

import org.orury.domain.comment.domain.dto.CommentDto;
import org.orury.domain.comment.domain.dto.CommentLikeDto;
import org.orury.domain.post.dto.PostDto;

import java.util.List;

public interface CommentService {
    void createComment(CommentDto commentDto);

    List<CommentDto> getCommentDtosByPost(PostDto postDto, Long cursor);

    List<CommentDto> getCommentDtosByUserId(Long userId, Long cursor);

    void updateComment(CommentDto commentDto);

    void deleteComment(CommentDto commentDto);

    void validate(CommentDto commentDto, Long userId);

    void validate(CommentLikeDto commentLikeDto);

    void validateParentComment(Long parentCommentId);

    CommentDto getCommentDtoById(Long id);

    void createCommentLike(CommentLikeDto commentLikeDto);

    void deleteCommentLike(CommentLikeDto commentLikeDto);

    boolean isLiked(Long userId, Long commentId);
}
