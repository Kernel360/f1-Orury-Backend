package org.fastcampus.oruryapi.domain.post.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryapi.base.converter.ApiResponse;
import org.fastcampus.oruryapi.domain.post.converter.request.PostLikeRequest;
import org.fastcampus.oruryapi.domain.post.service.PostLikeService;
import org.fastcampus.oruryapi.global.message.info.InfoMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping
@RestController
public class PostLikeController {
    private final PostLikeService postLikeService;

    @PutMapping("/post/like")
    public ApiResponse<Object> createPostLike(@RequestBody PostLikeRequest postLikeRequest) {
        postLikeService.createPostLike(postLikeRequest);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(InfoMessage.POST_LIKE_CREATED.getMessage())
                .build();
    }

    @DeleteMapping("/post/like")
    public ApiResponse<Object> deletePostLike(@RequestBody PostLikeRequest postLikeRequest) {
        postLikeService.deletePostLike(postLikeRequest);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(InfoMessage.POST_LIKE_DELETED.getMessage())
                .build();
    }
}
