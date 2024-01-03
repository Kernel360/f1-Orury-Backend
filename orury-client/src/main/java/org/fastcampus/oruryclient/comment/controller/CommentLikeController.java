package org.fastcampus.oruryclient.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryclient.comment.converter.request.CommentLikeRequest;
import org.fastcampus.oruryclient.comment.service.CommentLikeService;
import org.fastcampus.oruryclient.comment.service.CommentService;
import org.fastcampus.oruryclient.comment.util.CommentMessage;
import org.fastcampus.oruryclient.global.constants.NumberConstants;
import org.fastcampus.orurydomain.base.converter.ApiResponse;
import org.fastcampus.orurydomain.comment.db.model.CommentLike;
import org.fastcampus.orurydomain.comment.db.model.CommentLikePK;
import org.fastcampus.orurydomain.comment.dto.CommentLikeDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping
@RestController
public class CommentLikeController {
    private final CommentLikeService commentLikeService;
    private final CommentService commentService;

    @Operation(summary = "댓글 좋아요 생성", description = "댓글 id를 받아, 댓글 좋아요를 생성한다.")
    @PostMapping("/comment/like")
    public ApiResponse<Object> createCommentLike(@RequestBody CommentLikeRequest commentLikeRequest) {
        CommentLikeDto commentLikeDto = CommentLikeDto.from(CommentLike.of(CommentLikePK.of(NumberConstants.USER_ID, commentLikeRequest.commentId())));
        commentService.isValidate(commentLikeDto);

        commentLikeService.createCommentLike(commentLikeDto);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(CommentMessage.COMMENT_LIKE_CREATED.getMessage())
                .build();
    }

    @Operation(summary = "댓글 좋아요 삭제", description = "댓글 id를 받아, 댓글 좋아요를 삭제한다.")
    @DeleteMapping("/comment/like")
    public ApiResponse<Object> deleteCommentLike(@RequestBody CommentLikeRequest commentLikeRequest) {
        CommentLikeDto commentLikeDto = CommentLikeDto.from(CommentLike.of(CommentLikePK.of(NumberConstants.USER_ID, commentLikeRequest.commentId())));
        commentService.isValidate(commentLikeDto);

        commentLikeService.deleteCommentLike(commentLikeDto);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(CommentMessage.COMMENT_LIKE_DELETED.getMessage())
                .build();
    }
}
