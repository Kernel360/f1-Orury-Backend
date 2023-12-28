package org.fastcampus.oruryclient.domain.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryclient.domain.base.converter.ApiResponse;
import org.fastcampus.oruryclient.domain.comment.converter.request.CommentLikeRequest;
import org.fastcampus.oruryclient.domain.comment.service.CommentLikeService;
import org.fastcampus.oruryclient.domain.comment.util.CommentMessage;
import org.fastcampus.oruryclient.global.constants.NumberConstants;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping
@RestController
public class CommentLikeController {
    private final CommentLikeService commentLikeService;

    @Operation(summary = "댓글 좋아요 생성", description = "댓글 id를 받아, 댓글 좋아요를 생성한다.")
    @PostMapping("/comment/like")
    public ApiResponse<Object> createCommentLike(@RequestBody CommentLikeRequest request) {
        // 추후 시큐리티로 ID 할당 필요
        Long userId = NumberConstants.USER_ID;
        commentLikeService.isValidate(userId, request.commentId());
        commentLikeService.createCommentLike(userId, request);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(CommentMessage.COMMENT_LIKE_CREATED.getMessage())
                .build();
    }

    @Operation(summary = "댓글 좋아요 삭제", description = "댓글 id를 받아, 댓글 좋아요를 삭제한다.")
    @DeleteMapping("/comment/like")
    public ApiResponse<Object> deleteCommentLike(@RequestBody CommentLikeRequest request) {
        // 추후 시큐리티로 ID 할당 필요
        Long userId = NumberConstants.USER_ID;
        commentLikeService.isValidate(userId, request.commentId());
        commentLikeService.deleteCommentLike(userId, request);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(CommentMessage.COMMENT_LIKE_DELETED.getMessage())
                .build();
    }
}
