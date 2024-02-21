package org.orury.client.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.client.comment.converter.message.CommentMessage;
import org.orury.client.comment.service.CommentLikeService;
import org.orury.client.comment.service.CommentService;
import org.orury.domain.base.converter.ApiResponse;
import org.orury.domain.comment.db.model.CommentLike;
import org.orury.domain.comment.db.model.CommentLikePK;
import org.orury.domain.comment.dto.CommentLikeDto;
import org.orury.domain.user.dto.UserPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments/like")
@RestController
public class CommentLikeController {
    private final CommentLikeService commentLikeService;
    private final CommentService commentService;

    @Operation(summary = "댓글 좋아요 생성", description = "댓글 id를 받아, 댓글 좋아요를 생성한다.")
    @PostMapping("/{commentId}")
    public ApiResponse<Object> createCommentLike(@PathVariable Long commentId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        CommentLikeDto commentLikeDto = CommentLikeDto.from(CommentLike.of(CommentLikePK.of(userPrincipal.id(), commentId)));
        commentService.isValidate(commentLikeDto);

        commentLikeService.createCommentLike(commentLikeDto);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(CommentMessage.COMMENT_LIKE_CREATED.getMessage())
                .build();
    }

    @Operation(summary = "댓글 좋아요 삭제", description = "댓글 id를 받아, 댓글 좋아요를 삭제한다.")
    @DeleteMapping("/{commentId}")
    public ApiResponse<Object> deleteCommentLike(@PathVariable Long commentId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        CommentLikeDto commentLikeDto = CommentLikeDto.from(CommentLike.of(CommentLikePK.of(userPrincipal.id(), commentId)));
        commentService.isValidate(commentLikeDto);

        commentLikeService.deleteCommentLike(commentLikeDto);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(CommentMessage.COMMENT_LIKE_DELETED.getMessage())
                .build();
    }
}
