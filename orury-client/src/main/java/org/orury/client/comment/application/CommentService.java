package org.orury.client.comment.application;

import org.orury.domain.comment.domain.dto.CommentDto;
import org.orury.domain.comment.domain.dto.CommentLikeDto;
import org.orury.domain.post.domain.dto.PostDto;

import java.util.List;

public interface CommentService {
    void createComment(CommentDto commentDto);

    List<CommentDto> getCommentDtosByPost(PostDto postDto, Long cursor);

    List<CommentDto> getCommentDtosByUserId(Long userId, Long cursor);

    void updateComment(CommentDto commentDto, Long userId);

    void deleteComment(CommentDto commentDto, Long userId);

    CommentDto getCommentDtoById(Long id);

    void createCommentLike(CommentLikeDto commentLikeDto);

    void deleteCommentLike(CommentLikeDto commentLikeDto);

    boolean isLiked(Long userId, Long commentId);
}
