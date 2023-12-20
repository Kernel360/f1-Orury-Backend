package org.fastcampus.oruryapi.domain.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryapi.base.converter.ApiResponse;
import org.fastcampus.oruryapi.domain.post.converter.request.PostLikeRequest;
import org.fastcampus.oruryapi.domain.post.service.PostLikeService;
import org.fastcampus.oruryapi.domain.post.util.PostMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping
@RestController
public class PostLikeController {
    private final PostLikeService postLikeService;

    @Operation(summary = "게시글 좋아요 생성", description = "유저 id와 게시글 id를 받아, 게시글 좋아요를 생성한다.")
    @PutMapping("/post/like")
    public ApiResponse<Object> createPostLike(@RequestBody PostLikeRequest postLikeRequest) {
        postLikeService.createPostLike(postLikeRequest);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(PostMessage.POST_LIKE_CREATED.getMessage())
                .build();
    }

    @Operation(summary = "게시글 좋아요 삭제", description = "유저 id와 게시글 id를 받아, 게시글 좋아요를 삭제한다.")
    @DeleteMapping("/post/like")
    public ApiResponse<Object> deletePostLike(@RequestBody PostLikeRequest postLikeRequest) {
        postLikeService.deletePostLike(postLikeRequest);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(PostMessage.POST_LIKE_DELETED.getMessage())
                .build();
    }
}
