package org.orury.client.comment.application;

import org.orury.client.comment.interfaces.request.CommentCreateRequest;
import org.orury.client.comment.interfaces.request.CommentUpdateRequest;
import org.orury.client.comment.interfaces.response.CommentResponse;
import org.orury.client.comment.interfaces.response.CommentsWithCursorResponse;
import org.orury.client.notification.application.NotificationService;
import org.orury.client.post.application.PostService;
import org.orury.client.user.application.UserService;
import org.orury.domain.comment.domain.dto.CommentDto;
import org.orury.domain.comment.domain.dto.CommentLikeDto;
import org.springframework.stereotype.Service;

import java.util.List;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentFacade {
    private final CommentService commentService;
    private final PostService postService;
    private final UserService userService;
    private final NotificationService notificationService;

    public void createComment(CommentCreateRequest request, Long userId) {
        var userDto = userService.getUserDtoById(userId);
        var postDto = postService.getPostDtoById(request.postId());
        var commentDto = request.toDto(userDto, postDto);
        commentService.createComment(commentDto);

        // TODO : 본인이 본인글에 댓글 단 것은 알림 안 가도록 validate 검사 필요함.
        // 정상적으로 알림이 가는지 확인하기 위해 예시로 추가했습니다.
        //notificationService.send(userDto, "작성한 게시글에 새로운 댓글이 달렸습니다.", "연결되는 url");
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
