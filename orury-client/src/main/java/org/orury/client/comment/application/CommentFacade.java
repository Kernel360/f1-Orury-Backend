package org.orury.client.comment.application;

import lombok.RequiredArgsConstructor;
import org.orury.client.comment.interfaces.request.CommentCreateRequest;
import org.orury.client.comment.interfaces.request.CommentUpdateRequest;
import org.orury.client.comment.interfaces.response.CommentResponse;
import org.orury.client.comment.interfaces.response.CommentsWithCursorResponse;
import org.orury.domain.comment.domain.dto.CommentDto;
import org.orury.domain.comment.domain.dto.CommentLikeDto;
import org.orury.domain.post.domain.PostService;
import org.orury.domain.user.domain.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentFacade {
    private final CommentService commentService;
    private final PostService postService;
    private final UserService userService;

    public void createComment(CommentCreateRequest request, Long userId) {
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
        var updatingCommentDto = request.toDto(commentDto);
        commentService.updateComment(updatingCommentDto, userId);
    }

    public void deleteComment(Long commentId, Long userId) {
        var commentDto = commentService.getCommentDtoById(commentId);
        commentService.deleteComment(commentDto, userId);
    }

    public void createCommentLike(CommentLikeDto commentLikeDto) {
        commentService.createCommentLike(commentLikeDto);
    }

    public void deleteCommentLike(CommentLikeDto commentLikeDto) {
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
