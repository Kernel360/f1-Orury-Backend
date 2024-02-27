package org.orury.client.comment.interfaces;

import lombok.RequiredArgsConstructor;
import org.orury.client.comment.application.request.CommentCreateRequest;
import org.orury.client.comment.application.request.CommentUpdateRequest;
import org.orury.client.comment.application.response.CommentResponse;
import org.orury.client.comment.application.response.CommentsWithCursorResponse;
import org.orury.client.post.service.PostService;
import org.orury.client.user.service.UserService;
import org.orury.domain.comment.domain.CommentService;
import org.orury.domain.comment.domain.dto.CommentDto;
import org.orury.domain.comment.domain.dto.CommentLikeDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentFacade {
    private final CommentService commentService;
    private final PostService postService;
    private final UserService userService;

    public void createComment(CommentCreateRequest request, Long userId) {
        commentService.validateParentComment(request.parentId());
        var userDto = userService.getUserDtoById(userId);
        var postDto = postService.getPostDtoById(request.postId());
        var commentDto = request.toDto(userDto, postDto);
        commentService.createComment(commentDto);
    }

    public CommentsWithCursorResponse getCommentsByPostId(Long postId, Long cursor, Long userId) {
        var postDto = postService.getPostDtoById(postId);
        var commentDtos = commentService.getCommentDtosByPost(postDto, cursor);
        return convertCommentDtosToCommentsWithCursorResponse(commentDtos, userId);
    }

    public void updateComment(CommentUpdateRequest request, Long userId) {
        var commentDto = commentService.getCommentDtoById(request.id());
        commentService.validate(commentDto, userId);
        var updatingCommentDto = request.toDto(commentDto);
        commentService.updateComment(updatingCommentDto);
    }

    public void deleteComment(Long commentId, Long userId) {
        var commentDto = commentService.getCommentDtoById(commentId);
        commentService.validate(commentDto, userId);
        commentService.deleteComment(commentDto);
    }

    public void createCommentLike(CommentLikeDto commentLikeDto) {
        commentService.validate(commentLikeDto);
        commentService.createCommentLike(commentLikeDto);
    }

    public void deleteCommentLike(CommentLikeDto commentLikeDto) {
        commentService.validate(commentLikeDto);
        commentService.deleteCommentLike(commentLikeDto);
    }

    private CommentsWithCursorResponse convertCommentDtosToCommentsWithCursorResponse(List<CommentDto> commentDtos, Long userId) {
        var commentResponses = commentDtos.stream()
                .map(commentDto -> {
                    boolean isLike = commentService.isLiked(userId, commentDto.id());
                    return CommentResponse.of(commentDto, userId, isLike);
                })
                .toList();
        return CommentsWithCursorResponse.of(commentResponses);
    }
}
